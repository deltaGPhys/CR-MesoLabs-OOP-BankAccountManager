package ATM.interfaces;

import ATM.accounts.Account;
import ATM.accounts.Checking;
import ATM.accounts.Investment;
import ATM.accounts.Savings;
import org.junit.Assert;
import org.junit.Test;

public class StoreableTest {

    @Test
    public void toStringArrayUser() {
        User user = new User("Jim","Brown","goolybib", 12, 12343);

        String[] actual = user.toStringArray();
        String[] expected = new String[] {
                "12",
                "Brown",
                "Jim",
                "12343",
                "goolybib"
        };

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void toStringArrayAccountChecking() {
        Account account = new Checking(12.23, 23, 3432);

        String[] actual = account.toStringArray();
        String[] expected = new String[] {
                "3432",
                "23",
                "12.23",
                "accounts.Checking",
                ""
        };

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void toStringArrayAccountSavings() {
        Account account = new Savings(12.23, 23, 3432, 0.05);

        String[] actual = account.toStringArray();
        String[] expected = new String[] {
                "3432",
                "23",
                "12.23",
                "accounts.Savings",
                "0.05"
        };

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void toStringArrayAccountInvestment() {
        Account account = new Investment(12.23, 23, 3432, 0.2);

        String[] actual = account.toStringArray();
        String[] expected = new String[] {
                "3432",
                "23",
                "12.23",
                "accounts.Investment",
                "0.2"
        };

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void saveToDB() {
    }
}