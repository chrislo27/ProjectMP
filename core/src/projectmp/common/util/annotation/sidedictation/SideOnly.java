package projectmp.common.util.annotation.sidedictation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used to show what networking side (client or server) the target is on.
 * <br>
 * Purely visual, therefore it has @Retention(RetentionPolicy.SOURCE)
 * 
 *
 */
@Retention(RetentionPolicy.SOURCE)
public @interface SideOnly {

	Side value();

}
