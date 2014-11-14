package org.talares.exceptions;

import org.talares.api.exceptions.TalaresException;

/**
 * @author Dennis Vis
 * @since 0.1.0
 */
public class TransformException extends TalaresException {

  public TransformException(Throwable error) {
    super(
        "Error while trying to apply transformation: \n" +
            stackTraceString(error.getStackTrace())
    );
  }

  private static String stackTraceString(StackTraceElement[] stackTrace) {

    final StringBuilder stringBuilder = new StringBuilder();

    for (final StackTraceElement element : stackTrace) {
      stringBuilder.append(element.toString()).append("\n");
    }

    return stringBuilder.toString();
  }
}
