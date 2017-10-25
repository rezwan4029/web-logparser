# tokup-scheduler
Job scheduling for various features of Toku-P


Steps to run 
-------------

Build project :
```bash 
mvn clean package
```

Run particular task : 
```bash 
java -cp target/tokup-shceduler.jar com.tokup.scheduler.tasks.reservationreminder.Task >> log/reservationreminder.log 2 >&1 
```

To add in crontab:
* Edit the crontab  `crontab -e`
* Write needed java command as a task. Here for example write 
```bash
java -cp target/tokup-shceduler.jar com.tokup.scheduler.tasks.reservationreminder.Task >> log/reservationreminder.log 2 >&1 
```
* To disable any cron task, comment that line by putting `#` at starting of line. 
* To view existing cron tasks. `crontab -l`
