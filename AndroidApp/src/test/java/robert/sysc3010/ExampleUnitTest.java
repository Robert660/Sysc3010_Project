package robert.sysc3010;

import android.content.Context;
import android.test.ActivityTestCase;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */




public class ExampleUnitTest extends AddContacts {

    Context mockContext;
    private AddContacts ac;

    @Before
    public void start(){
       ac = new AddContacts();

    }
   @Test
    public void register_test() throws Exception{

       ac.addNewNumber("A","");
       ac.addNewNumber("A","");
       ac.addNewNumber("A","");
       ac.addNewNumber("A","");
       int correctSize = 0;

       assertThat(ac.size,is(correctSize));






   }


}