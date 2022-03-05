package net.rncmobile.fmdrivetest.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by cedric_f25 29/04/20.
 */

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceContext {
}
