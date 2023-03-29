package tiduswr.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import tiduswr.utils.DataUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckDateDifferenceMatcher extends TypeSafeMatcher<Date> {

    private final Date CHECK_DATE;

    public CheckDateDifferenceMatcher(Date checkDate){
        this.CHECK_DATE = checkDate;
    }

    public CheckDateDifferenceMatcher(Date checkDate, Integer plusDays){
        this.CHECK_DATE = DataUtils.adicionarDias(checkDate, plusDays);
    }

    @Override
    protected boolean matchesSafely(Date item) {
        return DataUtils.isMesmaData(item, CHECK_DATE);
    }

    @Override
    public void describeTo(Description description) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        description.appendText(format.format(CHECK_DATE));
    }
}
