//Victoria Severin
//1539768
//February 26,2018
//Simulation.java
//Implement a Queue ADT in Java based on a linked list data structure. Use the
//Queue ADT to simulate a set of jobs performed by a set of processors where
//there are more jobs than processors and therefore some jobs may have to wait in a queue.




//1.check command line arguments
//2.open files for reading and writing
//3.read in m jobs from input file
//4.run simulation with n processors for n=1 to n=m-1  {
//5.declare and initialize an array of n processor Queues and any 
//necessary storage Queues
//6.while unprocessed jobs remain  {
//7.determine the time of the next arrival or finish event and 
//update time
//8..complete all processes finishing now
//9.if there are any jobs arriving now, assign them to a processor 
//Queue of minimum length and with lowest index in the queue array.
//10.     } end loop
//11.compute the total wait, maximum wait, and average wait for 
//all Jobs, then reset finish times
//12. } end loop
//13. close input and output files



import java.io.*;
import java.util.Scanner;

public class Simulation {

    public static void main(String[] args) throws IOException {

        /* Handler to input file */
        Scanner scanner = null;

        /* Handlers to file writers */
        PrintWriter reportWriter = null;
        PrintWriter traceWriter = null;

        /* Intermediary Storage Queue */
        Queue intermStorageQueue = new Queue();

        /* Main Storage Queue */
        Queue mainStorageQueue = new Queue();

        /* Completed Storage Queue */
        Queue completedStorageQueue = new Queue();

        /* Processing Queues */
        Queue[] procQueues = null;

        /* Number of jobs as read from the input file */
        int numOfJobs = 0;

        /* Total time */
        int time = 0;

        /* Read the command line argument and show usage format */
        try {
            if (args.length != 1) {
                System.out.println("Usage: Simultation input_file");
                System.exit(1);
            }

            /* Read input file */
            scanner = new Scanner(new File(args[0]));

            /* Initialize file writers */
            reportWriter = new PrintWriter(new FileWriter(args[0] + ".rpt"));
            traceWriter = new PrintWriter(new FileWriter(args[0] + ".trc"));

        } catch(FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Get the number of jobs
        numOfJobs = numOfJobs(scanner);

        // Job handler
        Job job = null;

        // Read all jobs and assign them to intermediary storage queue
        while (scanner.hasNextLine()) {
            intermStorageQueue.enqueue(getJob(scanner));
        }


        // Begin tracing and reporting
        traceWriter.println("Trace file: " + (args[0] + ".trc"));
        traceWriter.println(numOfJobs + " Jobs:");
        traceWriter.println(intermStorageQueue.toString());
        traceWriter.println();

        reportWriter.println("Report file: " + (args[0] + ".rpt"));
        reportWriter.println(numOfJobs + " Jobs:");
        reportWriter.println(intermStorageQueue.toString());
        reportWriter.println();
        reportWriter.println("--------");

        // Begin simulation
        // Creating the number of processes which is one less than the number of jobs
        for (int n = 1; n <  numOfJobs; n++) {

            /* Data to collect */
            int totalWaitTime = 0;
            int maxWaitTime = 0;
            double avgWaitTime = 0.0;

            for(int i = 1; i < intermStorageQueue.length()+1; i++) {
                job = (Job)intermStorageQueue.dequeue();
                job.resetFinishTime();

                // Add this job to the main storage
                mainStorageQueue.enqueue(job);

                // And also add it back to intermediary storage
                intermStorageQueue.enqueue(job);
            }

            /* Initialize process queues. */
            int processors = n;
            procQueues = new Queue[n + 2];
            procQueues[0] = mainStorageQueue;
            procQueues[n + 1] = completedStorageQueue;

            for(int i = 1; i < n + 1; i++) {
                procQueues[i] = new Queue();
            }

            // Record the number of processors
            traceWriter.println("-----------------------------");
            traceWriter.println(processors + " processors:");
            traceWriter.println("-----------------------------");

            // Record the time
            traceWriter.println("time = " + time);

            // Record processors
            traceWriter.println("0: " + mainStorageQueue.toString());
            for(int i = 1; i < processors + 1; i++) {
                traceWriter.println(i + ": " + procQueues[i]);
            }

            /* Check for the pending jobs */
            while(completedStorageQueue.length() !=  numOfJobs) {

                int compFinal = Integer.MAX_VALUE;
                int finalIndex = 1;
                int completed  = -1;
                int length = -1;
                int finalLength = -1;
                Job compJob = null;


                /* Checks job arrival time */
                if (!mainStorageQueue.isEmpty()) {
                    compJob = (Job)mainStorageQueue.peek();
                    compFinal = compJob.getArrival();
                    finalIndex = 0;
                }

                for(int i = 1; i < processors+1; i++) {
                    if (procQueues[i].length() != 0) {
                        compJob = (Job)procQueues[i].peek();
                        completed = compJob.getFinish();
                    }

                    if (completed == -1) {

                    } else if (completed < compFinal) {
                        compFinal = completed;
                        finalIndex = i;
                    }
                    time = compFinal;
                }

                if (finalIndex == 0) {
                    int tempIndex = 1;
                    finalLength = procQueues[tempIndex].length();
                    for (int i = 1; i < processors+1; i++) {
                        length = procQueues[i].length();
                        if (length < finalLength) {
                            finalLength = length;
                            tempIndex = i;
                        }
                    }

                    /* Dequeue - main storage will have one less job */
                    compJob = (Job)mainStorageQueue.dequeue();
                    procQueues[tempIndex].enqueue(compJob);

                    if (procQueues[tempIndex].length() == 1) {
                        compJob = (Job)procQueues[tempIndex].peek();
                        compJob.computeFinishTime(time);
                    }

                } else {
                    compJob = (Job)procQueues[finalIndex].dequeue();
                    completedStorageQueue.enqueue(compJob);
                    int tempWait = compJob.getWaitTime();
                    if (tempWait > maxWaitTime)
                        maxWaitTime = tempWait;
                    totalWaitTime += tempWait;

                    if (procQueues[finalIndex].length() >= 1) {
                        compJob = (Job)procQueues[finalIndex].peek();
                        compJob.computeFinishTime(time);
                    }
                }

                // Print traces
                traceWriter.println();
                traceWriter.println("time=" + time);
                traceWriter.println("0: " + mainStorageQueue.toString());

                for(int i = 1; i < processors+1; i++)
                    traceWriter.println(i + ": " + procQueues[i]);
            }

            avgWaitTime = ((double)totalWaitTime/numOfJobs);
            avgWaitTime = (double)Math.round(avgWaitTime * 100)/100;
            traceWriter.println();

            reportWriter.println(processors + " processors: totalWait=" + totalWaitTime + ", maxWait=" + maxWaitTime + ", averageWait=" + avgWaitTime);

            time = 0;
            completedStorageQueue.dequeueAll();
        }

        // Properly close the handlers
        scanner.close();
        reportWriter.close();
        traceWriter.close();

    }

    /* Determine the number of jobs to be completed */
    private static int numOfJobs(Scanner scanner) {
        return Integer.parseInt(scanner.nextLine());
    }

//referenced from simulationStub.java
    private static Job getJob(Scanner scanner) {
        String[] s = scanner.nextLine().split(" ");
        int a = Integer.parseInt(s[0]);
        int d = Integer.parseInt(s[1]);
        return new Job(a, d);
    }

}
