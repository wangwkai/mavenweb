import com.smbms.pojo.User;
import com.smbms.service.user.UserService;
import com.smbms.utils.PageSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Hunter on 2020-06-01.
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    public void test() throws Exception {
        User user = userService.login("admin", "1234567");
        System.out.println(user);
    }

    @Test
    public void test2() throws Exception {
        PageSupport<User> pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(2);

        String userName = null;
        Integer roleId = 0;
        userService.getUsersPage(pageSupport, userName, roleId);
        //System.out.println(pageSupport);
        System.out.println("当前页：" + pageSupport.getCurrentPageNo());
        System.out.println("总页数：" + pageSupport.getTotalPageCount());
        System.out.println("数据列表：");
        for (User user : pageSupport.getList()) {
            System.out.println(user);
        }

    }



}
