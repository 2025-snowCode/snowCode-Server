package snowcode.snowcode.chatRoom.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.service.MemberService;
import snowcode.snowcode.chat.domain.Chat;
import snowcode.snowcode.chat.dto.ChatListResponse;
import snowcode.snowcode.chat.service.ChatService;
import snowcode.snowcode.chatRoom.domain.ChatRoom;
import snowcode.snowcode.chatRoom.dto.ChatRoomListResponse;
import snowcode.snowcode.chatRoom.dto.ChatRoomListResponseWithCount;
import snowcode.snowcode.chatRoom.dto.ChatRoomResponse;
import snowcode.snowcode.chatRoomUser.domain.ChatRoomUser;
import snowcode.snowcode.chatRoomUser.service.ChatRoomUserService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomFacade {

    private final ChatRoomService chatRoomService;
    private final ChatRoomUserService chatRoomUserService;
    private final ChatService chatService;
    private final MemberService memberService;

    @Transactional
    public void createChatRoom(Member admin, Member user) {
        // 채팅방 만들기
        ChatRoom chatRoom = chatRoomService.createChatRoom();
        // 채팅 참여자 추가
        chatRoomUserService.createChatRoomUser(admin, user, chatRoom);
    }

    public ChatRoomResponse findChatRoomByChatId(Member member, Long chatRoomId) {
        // chatId로 ChatRoomUser 찾기 (2명)
        List<ChatRoomUser> chatRoomUserList = chatRoomUserService.findChatRoomUserByChatRoomId(chatRoomId);
        // 찾은 ChatRoomUser 중 내가 아닌 상대 찾아 반환 (opponent)
        ChatRoomUser opponentUser = chatRoomUserList.get(0).getMember().equals(member)
                ? chatRoomUserList.get(1)
                : chatRoomUserList.get(0);

        Member opponent = opponentUser.getMember();

        // id로 채팅룸 찾기
        ChatRoom chatRoom = chatRoomService.findByChatRoomId(chatRoomId);
        // 메시지 리스트 찾아서 리턴 (chatRoomId로 모든 메시지 찾기)
        List<Chat> chatList = chatService.findChatListByChatRoomId(chatRoomId);
        List<ChatListResponse> chatDtoList = chatList.stream().map(ChatListResponse::of).toList();
        return ChatRoomResponse.of(opponent, chatRoom, chatDtoList);
    }

    public ChatRoomListResponseWithCount findChatRoomList(Member member) {
        // memberId로 ChatRoomUser list 찾기
        List<ChatRoomUser> myChatRoomList = chatRoomUserService.findListByMemberId(member.getId());

        // chatRoomIdList로 변환
        List<ChatRoom> chatRoomList = myChatRoomList.stream().map(ChatRoomUser::getChatRoom).toList();
        List<Long> chatRoomIdList = chatRoomList.stream().map(ChatRoom::getId).toList();

        // chatRoomIdList로 상대 회원 id 찾기 (한 번에) -> map으로 매칭 (Map<채팅방Id, ChatRoomListResponse>)
        Map<Long, ChatRoomListResponse> mappedChatIdAndMemberId = chatRoomUserService.findMemberIdByChatId(member, chatRoomIdList);

        // chatRoomListResponse만 List로 변환
        List<ChatRoomListResponse> chatRoomListResponseList = new ArrayList<>(mappedChatIdAndMemberId.values());
        chatRoomListResponseList.sort(
                Comparator.comparing(
                        ChatRoomListResponse::lastSentAt,
                        Comparator.nullsLast(Comparator.reverseOrder())
                )
        );

        // 최신순으로 정렬
        return ChatRoomListResponseWithCount.of(chatRoomListResponseList);
    }
}
