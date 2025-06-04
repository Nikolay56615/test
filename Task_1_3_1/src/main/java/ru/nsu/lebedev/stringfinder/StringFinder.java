package ru.nsu.lebedev.stringfinder;

/**
 * StringFinder child class with finding method.
 */
public class StringFinder extends AbstractStringFinder {

    public StringFinder() {
        super();
    }

    @Override
    protected void findingSubstring() {
        long filePosition = 0;
        String previousSegmentPostfix = "";
        while (readSegment() != -1) {
            buffer.insert(0, previousSegmentPostfix);
            int bufferLength = buffer.length();
            int targetLength = searchTarget.length();
            if (bufferLength < targetLength) {
                break;
            }
            int bufferIndex = buffer.indexOf(searchTarget);
            while (bufferIndex != -1) {
                targetsPositions.add(filePosition + bufferIndex);
                bufferIndex = buffer.indexOf(searchTarget, bufferIndex + 1);
            }
            previousSegmentPostfix = buffer.substring(bufferLength - targetLength + 1);
            bufferLength = buffer.length();
            filePosition += bufferLength - previousSegmentPostfix.length();
        }
    }
}
