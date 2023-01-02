import com.seamew.dataBinder.User;
import com.seamew.dataBinder.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;

import java.util.List;

@Slf4j
public class TestDataBinder
{
    @Test
    public void testDataBinder()
    {
        User user = new User("12345678900");
        DataBinder dataBinder = new DataBinder(user);
        dataBinder.setValidator(new UserValidator());
        dataBinder.validate();

        BindingResult bindingResult = dataBinder.getBindingResult();
        List<ObjectError> errors = bindingResult.getAllErrors();
        for (ObjectError error : errors)
        {
            log.debug("The error is [{}]", error);
        }
    }
}
