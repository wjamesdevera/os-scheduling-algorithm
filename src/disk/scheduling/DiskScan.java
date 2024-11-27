package disk.scheduling;

import java.util.ArrayList;
import java.util.Collections;

public class DiskScan {
    private final int TRACK_SIZE;
    private final int SEEK_RATE;
    private ArrayList<Integer> seekSequence = new ArrayList<>();

    public ArrayList<Integer> getSeekSequence() {
        return seekSequence;
    }

    public DiskScan(int TRACK_SIZE, int SEEK_RATE) {
        this.TRACK_SIZE = TRACK_SIZE;
        this.SEEK_RATE = SEEK_RATE;
    }

    public int scan(int[] requests, int head, Direction direction) {
        int seekCount = 0;
        int distance, currentTrack;
        ArrayList<Integer> left = new ArrayList<>();
        ArrayList<Integer> right = new ArrayList<>();

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

        for (int i = 0; i < RUN_ROUNDS; i++) {
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
        return seekCount;
    }

    public void setSeekSequence(ArrayList<Integer> seekSequence) {
        this.seekSequence = seekSequence;
    }

}
