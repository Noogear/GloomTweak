package cn.gloomTweak.utils.SchedulerUtil;

public interface SchedulerInterface {

    void cancelAll();

    /**
     * Run a task later
     *
     * @param task  The task to run
     * @param delay The delay in ticks (20 ticks = 1 second)
     */
    void runTaskLater(Runnable task, long delay);

    /**
     * Run a task
     *
     * @param task The task to run
     */
    void runTask(Runnable task);

    /**
     * Run a task later asynchronously
     *
     * @param task  The task to run
     * @param delay The delay in milliseconds
     */
    void runTaskLaterAsync(Runnable task, long delay);

    /**
     * Run a task asynchronously
     *
     * @param task The task to run
     */
    void runTaskAsync(Runnable task);

}
