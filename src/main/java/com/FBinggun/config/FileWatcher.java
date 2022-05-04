package com.FBinggun.config;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.nio.file.Files;

public abstract class FileWatcher {


    private long lastModifiedTime =0L;
    protected final File file;
    private final BukkitTask task;
    public FileWatcher(JavaPlugin plugin,File file){
        this.file=file;
        task=plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::updateLastModifiedTime,0,20*10);
    }

    public void updateLastModifiedTime() {
        try {
            long lastModifiedTimePre=lastModifiedTime;
            lastModifiedTime = Files.getLastModifiedTime(file.toPath()).toMillis();
            if(file.exists()&&lastModifiedTimePre!=0L) {
                if(lastModifiedTimePre!=lastModifiedTime){
                    onModified();
                }
            }
        } catch (Throwable ignored) {
        }
    }
    protected abstract void onModified();
    public void shutdown(){
        if(!task.isCancelled()){
            task.cancel();
        }
    }
}
