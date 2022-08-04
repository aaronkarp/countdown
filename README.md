# countdown
AEM implementation of The Final Countdown (http://hilios.github.io/jQuery.countdown/). This may be the first AEM component I built from the ground up. The Java model, the HTL, the clientlibs, and the authoring dialog are all mine.

I wrote the Java model to circumvent AEM's default behavior of respecting the author's time zone when they enter a time. In this component, the time is forced to be US Eastern Time, complete with Daylight Savings consideration, as all Carter's countdowns are based on Eastern Time.
