package com.pailsom.flare.batch.controller;

import com.pailsom.flare.batch.model.JobConfig;
import com.pailsom.flare.batch.service.JobRunningService;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.math.NumberUtils.isParsable;

@RestController
@RequestMapping("/jobs")
public class JobController {
    private final Pattern jobParameterPattern = Pattern.compile("([^=]+)=([^=]+)");

    private final JobRunningService jobRunningService;


    @Autowired
    public JobController(JobRunningService jobRunningService) {
        this.jobRunningService = jobRunningService;
    }


    @PostMapping(value = "/run",consumes = "application/json", produces = "application/json")
    public void run(@RequestBody JobConfig jobConfig) {
        Assert.notNull(jobConfig.getJobName(),"Job name required field");

        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();

        jobConfig.getParam().entrySet().forEach( entry-> {
                    jobParametersBuilder.addParameter(entry.getKey(), parseJobParameter(entry.getValue()));
                }
        );

        if (jobConfig.isForceRun()) {
            jobParametersBuilder.addLong("uniqueId", System.currentTimeMillis());
        }
        jobRunningService.runJob(jobConfig.getJobName(), jobParametersBuilder.toJobParameters());
    }

    private JobParameter parseJobParameter(String argument) {
        if (isParsable(argument)) {
            if (argument.contains(".")) {
                return new JobParameter(Double.parseDouble(argument));
            } else {
                return new JobParameter(Long.parseLong(argument));
            }
        }
        return new JobParameter(argument);
    }

    /**
     * Get API exposed to run Job now
     * @param jobname
     */
    @GetMapping(value = "/run-now/{jobName}",produces = "application/json")
    public void runNow(@PathVariable("jobName") String jobname) {
        JobConfig jobConfig = new  JobConfig();
        jobConfig.setJobName(jobname);
        jobConfig.setParam(new HashMap<String, String>());
        jobConfig.setForceRun(true);
        run(jobConfig);
    }

}
