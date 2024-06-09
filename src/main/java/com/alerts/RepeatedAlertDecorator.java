package com.alerts;

public class RepeatedAlertDecorator extends AlertDecorator
{
    public RepeatedAlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert);
    }
    public void recheckAlert()
    {
        //TODO implement recheck
    }
}
