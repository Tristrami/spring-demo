import com.seamew.beanWrapper.Company;
import com.seamew.beanWrapper.Employee;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyValue;

/* BeanWrapper 接口提供了设置和获取属性值, 获取属性描述符等功能.
   BeanWrapper 提供了对嵌套属性的支持, 允许对子属性进行无限深度的检索 */

@Slf4j
public class TestBeanWrapper
{
    @Test
    public void testBeanWrapperImpl()
    {
        // 为 Company 创建一个 BeanWrapper
        BeanWrapper companyWrapper = new BeanWrapperImpl(new Company());
        // 为 name 属性赋值
        companyWrapper.setPropertyValue("name", "Apple Inc.");
        // 也可以使用这种方式给 name 赋值
        PropertyValue propertyValue = new PropertyValue("name", "Apple Inc.");
        companyWrapper.setPropertyValue(propertyValue);

        // 为 Employee 创建一个 BeanWrapper, 并把它绑定到 Company 中
        BeanWrapper employeeWrapper = new BeanWrapperImpl(new Employee());
        employeeWrapper.setPropertyValue("name", "Steve");
        companyWrapper.setPropertyValue("managingDirector", employeeWrapper.getWrappedInstance());

        // 用嵌套的方式设置 company 中 employee 的 salary 属性
        companyWrapper.setPropertyValue("managingDirector.salary", 1000000);

        log.debug("The company is [{}]", companyWrapper.getWrappedInstance());
    }
}
