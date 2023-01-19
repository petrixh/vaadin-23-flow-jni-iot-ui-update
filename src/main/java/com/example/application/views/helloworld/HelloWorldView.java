package com.example.application.views.helloworld;

import com.example.application.service.SocketServicePOC;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@PageTitle("Hello World")
@Route(value = "hello")
@RouteAlias(value = "")
public class HelloWorldView extends VerticalLayout implements SocketServicePOC.JniSocketServiceObserver {

    private final VerticalLayout jniEventsLayout;
    private TextField name;
    private Button sayHello;
    private SocketServicePOC socketServicePOC;

    public HelloWorldView(@Autowired SocketServicePOC socketServicePOC) {
        this.socketServicePOC = socketServicePOC;

        HorizontalLayout form = new HorizontalLayout();
        add(form);

        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });
        sayHello.addClickShortcut(Key.ENTER);

        setMargin(true);
        form.add(name, sayHello);
        form.setVerticalComponentAlignment(Alignment.END, name, sayHello);

        jniEventsLayout = new VerticalLayout();
        add(jniEventsLayout);

        //When this view is visible, we attach the observer so we get notified of events...
        addAttachListener(attachEvent -> {
            socketServicePOC.addJniSocketServiceObserver(this);
        });

        //When the UI is no longer visible remove the UI observer
        // (service could still observe if it wanted to)
        addDetachListener(detachEvent -> {
            socketServicePOC.removeJniObserver(this);
        });

    }

    @Override
    public void onMessage(String message) {

        //Only update the UI state if it's visible...
        Optional<UI> uiOptional = getUI();
        if(uiOptional.isPresent()) {
            UI ui = uiOptional.get();

            //Threadsafe way of updating the UI state in Vaadin...
            ui.access(() -> {
                //We use divs just to make each entry its own line... Could also use Text or Label...
                Div uiMessage = new Div();
                uiMessage.setText("JniIotClient sent: " + message);

                //Adding as first, so that the latest message is at the top...
                jniEventsLayout.addComponentAsFirst(uiMessage);
            });
        }

    }
}
