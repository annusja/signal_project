package com.alerts;

public class AlertDecorator extends Alert{
    protected Alert decoratedAlert;

    public AlertDecorator(Alert decoratedAlert)
    {
        super(decoratedAlert.getPatientId(), decoratedAlert.getCondition(), decoratedAlert.getTimestamp());
    }
}
