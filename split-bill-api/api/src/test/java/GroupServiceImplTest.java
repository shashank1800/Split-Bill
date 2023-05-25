import com.api.dto.groups.GroupsEntityDto;
import com.api.dto.groups.GroupsSaveDto;
import com.api.service.impl.GroupServiceImpl;
import com.api.service.impl.UserProfileServiceImpl;
import com.common.exception.KnownException;
import com.common.util.Valid;
import com.data.entity.GroupsEntity;
import com.data.entity.UserProfileEntity;
import com.data.entity.UsersEntity;
import com.data.repository.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@SpringBootTest(classes = {GroupServiceImpl.class})
@RunWith(SpringRunner.class)
//@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
public class GroupServiceImplTest {

    @Mock
    private GroupServiceImpl groupService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private UserProfileServiceImpl userProfileService;

    @BeforeEach
    public void init() {

    }

    @Test
    @DisplayName("Testing saveGroup method of GroupServiceImpl")
    public void testSaveGroup() throws KnownException {
        // Create mock objects
        final Integer createdBy = 123;
        final Integer memberId1 = 456;
        final Integer memberId2 = 789;
        final UserProfileEntity mockUser1 = Mockito.mock(UserProfileEntity.class);
        final UserProfileEntity mockUser2 = Mockito.mock(UserProfileEntity.class);
        final List<Integer> peoples = Arrays.asList(memberId1, memberId2);
        final ArrayList<UserProfileEntity> userProfiles = new ArrayList<>();
        userProfiles.add(mockUser1);
        userProfiles.add(mockUser2);

        Mockito.when(userProfileService.getProfile(memberId1)).thenReturn(mockUser1);
        Mockito.when(userProfileService.getProfile(memberId2)).thenReturn(mockUser2);
        Mockito.when(usersRepository.saveAll(Mockito.anyList()))
                .thenReturn(Arrays.asList(
                        new UsersEntity(),
                        new UsersEntity(),
                        new UsersEntity()
                ));

        // Execute code
        final GroupsSaveDto groupToSave = new GroupsSaveDto("Test Group", peoples);
        final Valid<GroupsEntity> expectedGroupsEntity = GroupsEntity.create(null, groupToSave.name,
                System.currentTimeMillis(), createdBy);

        final GroupsEntity actualGroupsEntity = groupService.addGroupAndUsers(
                expectedGroupsEntity.getValue(),
                new ArrayList<>()
        );
        final GroupsEntityDto expectedGroupsEntityDto = new GroupsEntityDto(actualGroupsEntity, new ArrayList<>());
        final GroupsEntityDto actualGroupsEntityDto = groupService.saveGroup(createdBy, groupToSave);

        // Assert
        Assertions.assertEquals(expectedGroupsEntityDto.getGroup().getName(),
                actualGroupsEntityDto.getGroup().getName());
        Assertions.assertTrue(expectedGroupsEntityDto.getUserList().isEmpty());
    }

    @Test
    @DisplayName("Testing getAllGroups method of GroupServiceImpl")
    public void testGetAllGroups() {

    }
}
