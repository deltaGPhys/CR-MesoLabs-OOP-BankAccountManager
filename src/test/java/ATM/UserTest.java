package ATM;

import ATM.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getCardNumberTest() {
        User user = new User("J","J","123",12,456);
        Assert.assertEquals(456,(int) user.getCardNumber());
    }

    @Test
    public void getPasswordTest() {
        User user = new User("J","J","123",12,456);
        Assert.assertEquals("123",user.getPassword());
    }
}