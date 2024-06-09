package com.alerts;

public class PriorityAlertDecorator extends AlertDecorator
{
    private String priorityLevel; //using enums would be nicer
    public PriorityAlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert);
    }
    public PriorityAlertDecorator(Alert decoratedAlert, String priorityLevel) {
        super(decoratedAlert);
        this.priorityLevel = priorityLevel;
    }
    public void setPriorityLevel(String priorityLevel)
    {
        this.priorityLevel = priorityLevel;
    }
    public void announcePriority()
    {
        System.out.println("this is a " + priorityLevel + " priority alert");
    }
}
