/**
 * UNFINISHED AS OF 2/13/2025
 *
 * This class will be called regularly at an increment specified
 * by the user or not at all. It will refresh the index to ensure
 * every search is accurate to the current versions of all files.
 */

package NotesArchive;
import org.json.simple.parser.ParseException;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;

public class IndexJob implements Job {
    static NotesArchive notes;

    public IndexJob(NotesArchive na, int interval) throws SchedulerException {
        notes = na;

        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        JobDetail jobDetail = JobBuilder.newJob()
                .ofType(IndexJob.class)
                .withIdentity("Index-Job")
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .forJob(jobDetail)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(interval)
                        .repeatForever())
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            notes.refreshIndex();
        } catch (IOException | ParseException | org.apache.lucene.queryparser.classic.ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
