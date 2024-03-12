package com.example.demo.service;

import com.example.demo.controller.DTO.GroupDTO;
import com.example.demo.controller.DTO.JoinedGroupDTO;
import com.example.demo.controller.util.RegexType;
import com.example.demo.exception.FormatException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.OperationException;
import com.example.demo.repository.GroupRepository;
import com.example.demo.repository.entity.GroupEntity;
import com.example.demo.repository.entity.GroupMemberEntity;
import com.example.demo.repository.entity.UserEntity;
import com.example.demo.repository.entity.type.JoinStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GroupServiceTests {
    private final long TEST_EXISTS_GORUP_ID = 1L;
    private final long TEST_OWNER_ID = 1L;

    private final long TEST_ANOTHER_USER_ID = 2L;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupMemberService groupMemberService;

    @InjectMocks
    private GroupService groupService;

    private GroupEntity mockGroup = new GroupEntity("test1", TEST_OWNER_ID);

    private GroupDTO mockGroupDTO;

    @BeforeEach
    public void setup(){
        mockGroupDTO = GroupDTO.createByEntity(mockGroup);
        when(groupRepository.existsById(TEST_EXISTS_GORUP_ID)).thenReturn(true);
        when(groupRepository.findById(TEST_EXISTS_GORUP_ID)).thenReturn(Optional.of(mockGroup));
    }

    @Test
    public void testFindGroupDTO(){
        assertDoesNotThrow(() -> {
            GroupDTO groupDTO = groupService.findGroupDTO(TEST_EXISTS_GORUP_ID);
            assertGroupEquals(mockGroupDTO, groupDTO);
        });
    }

    @Test
    public void testCreateGroup() {
        final String GROUP_NAME = "TestGroup";

        assertDoesNotThrow(() -> {
            GroupDTO createdGroupDTO = groupService.createGroup(GROUP_NAME, TEST_OWNER_ID);
            assertNotNull(createdGroupDTO);
            assertEquals(GROUP_NAME, createdGroupDTO.getName());
            assertEquals(TEST_OWNER_ID, createdGroupDTO.getOwnerId());
            verify(groupRepository)
                    .save(argThat(groupEntity -> groupEntity.getName().equals(GROUP_NAME) &&
                                                 groupEntity.getOwnerId() == TEST_OWNER_ID));
            verify(groupMemberService)
                    .createGroupMember(TEST_OWNER_ID, createdGroupDTO.getId(), JoinStatus.JOIN);
        });
    }

    @Test
    public void testCreateGroup_formatException() {
        final String GROUP_NAME = "TestGroupTestGroupTestGroupTestGroupTestGroupTestGroupTestGroup";
        assertThrows(FormatException.class, () -> {
            groupService.createGroup(GROUP_NAME, TEST_OWNER_ID);
            verify(groupRepository, never()).save(any());
            verify(groupMemberService, never()).createGroupMember(anyLong(), anyLong(), any());
        });
    }

    @Test
    public void testValidateGroupAdmin() {
        assertDoesNotThrow(() -> {
            groupService.validateGroupAdmin(TEST_OWNER_ID, mockGroupDTO);
        });
    }

    @Test
    public void testValidateGroupAdmin_Error() {
        assertThrows(OperationException.class, () -> {
            groupService.validateGroupAdmin(TEST_ANOTHER_USER_ID, mockGroupDTO);
        });
    }

    @Test
    public void testUpdateGroupOwner_successful() {
        assertDoesNotThrow(() -> {
            groupService.updateGroupOwner(TEST_OWNER_ID, TEST_ANOTHER_USER_ID, mockGroupDTO);
            verify(groupRepository).updateOwnerById(mockGroupDTO.getId(), TEST_ANOTHER_USER_ID);
        });
    }

    @Test
    public void testDeleteGroup() {
        assertDoesNotThrow(() -> {
            groupService.deleteGroup(TEST_OWNER_ID, mockGroupDTO);
            verify(groupRepository).deleteById(mockGroupDTO.getId());
        });
    }

    private void assertGroupEquals(GroupDTO expected, GroupDTO actual){
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getOwnerId(), actual.getOwnerId());
    }

}
