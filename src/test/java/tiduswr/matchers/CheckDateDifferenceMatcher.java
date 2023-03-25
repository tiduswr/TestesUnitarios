package tiduswr.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import tiduswr.utils.DataUtils;

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
        description.appendText(CHECK_DATE.toString());
    }
}
