package com.pailsom.flare.batch.listener;

import lombok.Data;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class FlareCacheManager {
    private Map<String,Object> cache = new HashMap<>();
}
