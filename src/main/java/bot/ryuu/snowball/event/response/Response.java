package bot.ryuu.snowball.event.response;

/**
 * pattern < action_name > _ < sub_action > _ < power_effect >
 * */
public enum Response {
    HIT,
    HIT_ENROLMENT,
    HIT_SUPER_THROW,

    MISSED,
    MISSED_ENROLMENT,

    RANDOM,

    NULL,
    ERROR,

    TAKE_SNOWBALL,
    TAKE_SNOWBALL_BOOST,
    TAKE_SNOWBALL_BIG_BAGS,
    TAKE_SNOWBALL_THIEF,

    THROW_SNOWBALL_LIMIT,

    TAKE_SNOWBALL_FORTUNE,

    STATS,
    ACTIVATE,

    TIMER_OVER
}