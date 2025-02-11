package NotesArchive;

import org.json.simple.parser.ParseException;
import org.quartz.Job;
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
        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(interval)
                        .repeatForever())
                .build();

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
