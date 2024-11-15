package disk.scheduling;

import java.util.ArrayList;
import java.util.Collections;

public class DiskScan {
    private final int TRACK_SIZE;
    private int SEEK_RATE;

    public DiskScan(int TRACK_SIZE, int SEEK_RATE) throws Exception {
        this.TRACK_SIZE = TRACK_SIZE;
        setSeekRate(SEEK_RATE);
    }

    private void printScanResult(int seekCount, ArrayList<Integer> seekSequence) {
        System.out.println("Total Head Movement: " + seekCount);
        for(int i = 0; i < seekSequence.size(); i ++) {
            System.out.println(i + " - " + seekSequence.get(i));
        }
    }

    public void scan(int[] requests, int head, Direction direction) {
        int seekCount = 0;
        int distance, currentTrack;
        ArrayList<Integer> left = new ArrayList<>();
        ArrayList<Integer> right = new ArrayList<>();
        ArrayList<Integer> seekSequence = new ArrayList<>();

        if (direction == Direction.LEFT) {
            left.add(0);
        } else if (direction == Direction.RIGHT) {
            right.add(TRACK_SIZE - 1);
        }

        for (int i = 0; i < SEEK_RATE; i++) {
            if (requests[i] < head) left.add(requests[i]);
            if (requests[i] > head) right.add(requests[i]);
        }

        Collections.sort(left);
        Collections.sort(right);

        int RUN_ROUNDS = 2;
        for (int i = 0; i < 2; i++) {
            if (direction == Direction.LEFT) {
                for (int j = left.size() - 1; j >= 0; j--) {
                    currentTrack = left.get(j);
                    seekSequence.add(currentTrack);
                    distance = Math.abs(currentTrack - head);
                    seekCount += distance;
                    head = currentTrack;
                }
                direction = Direction.RIGHT;
            } else if (direction == Direction.RIGHT) {
                for (int j = 0; j < right.size(); j++) {
                    currentTrack = right.get(j);
                    seekSequence.add(currentTrack);
                    distance = Math.abs(currentTrack - head);
                    seekCount += distance;
                    head = currentTrack;
                }
                direction = Direction.LEFT;
            }
        }
        printScanResult(seekCount, seekSequence);
    }

    private void setSeekRate(int seekRate) throws Exception {
        int MAX_SEEK_RATE = 10;
        final String ERROR_MESSAGE = "SEEK_RATE EXCEEDS MAX_SEEK_RATE:" + MAX_SEEK_RATE;
        if (seekRate > MAX_SEEK_RATE) {
            throw new Exception(ERROR_MESSAGE);
        }
        this.SEEK_RATE = seekRate;
    }
}
