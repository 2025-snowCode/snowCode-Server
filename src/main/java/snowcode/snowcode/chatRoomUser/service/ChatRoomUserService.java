package snowcode.snowcode.chatRoomUser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.chatRoom.domain.ChatRoom;
import snowcode.snowcode.chatRoom.dto.ChatRoomListResponse;
import snowcode.snowcode.chatRoomUser.domain.ChatRoomUser;
import snowcode.snowcode.chatRoomUser.repository.ChatRoomUserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomUserService {

    private final ChatRoomUserRepository chatRoomUserRepository;

    @Transactional
    public void createChatRoomUser(Member admin, Member user, ChatRoom chatRoom) {
        ChatRoomUser chatRoomUserByAdmin = ChatRoomUser.createChatRoomUser(admin, chatRoom);
        ChatRoomUser chatRoomUserByUser = ChatRoomUser.createChatRoomUser(user, chatRoom);
        chatRoomUserRepository.saveAll(List.of(chatRoomUserByAdmin, chatRoomUserByUser));
    }

    public List<ChatRoomUser> findChatRoomUserByChatRoomId(Long chatRoomId) {
        return chatRoomUserRepository.findByChatRoomId(chatRoomId);
    }

    public List<ChatRoomUser> findListByMemberId(Long memberId) {
        return chatRoomUserRepository.findByMemberId(memberId);
    }

    // Map<채팅방 id, ChatRoomListResponse>
    public Map<Long, ChatRoomListResponse> findMemberIdByChatId(Member member, List<Long> chatRoomIdList) {
        // 채팅방 id로 채팅 참여자 모두 찾기
        List<ChatRoomUser> chatRoomUserList = chatRoomUserRepository.findAllByChatRoomIdIn(chatRoomIdList);
        // 그 중 member인 사람을 제외하고 상대 id를 찾아서 mapping

        // Map<채팅방 id, ChatRoomListResponse>
        Map<Long, ChatRoomListResponse> map = new HashMap<>();

        for (ChatRoomUser cru : chatRoomUserList) {
            // 자신이면 제외, 상대방만 카운트
            if (cru.getMember().getId().equals(member.getId())) continue;

            Long chatRoomId = cru.getChatRoom().getId();
            map.put(chatRoomId, ChatRoomListResponse.of(cru.getChatRoom(), cru.getMember()));
        }
        return map;
    }

}
