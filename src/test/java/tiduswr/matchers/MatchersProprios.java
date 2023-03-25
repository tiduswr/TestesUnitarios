package tiduswr.matchers;

import java.util.Calendar;
import java.util.Date;

public class MatchersProprios {
    public static DiaSemanaMatcher caiEm(Integer diaSemana){
        return new DiaSemanaMatcher(diaSemana);
    }

    public static DiaSemanaMatcher caiEmUmaSegunda(){
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }

    public static CheckDateDifferenceMatcher isHoje(){
        return new CheckDateDifferenceMatcher(new Date());
    }

    public static CheckDateDifferenceMatcher isHojePlusDays(Integer plusDays){
        return new CheckDateDifferenceMatcher(new Date(), plusDays);
    }

}
