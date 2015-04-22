package com.guesswhat.server.persistence.jpa.cfg;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.guesswhat.server.persistence.jpa.entity.Image;
import com.guesswhat.server.persistence.jpa.entity.ImageHolder;
import com.guesswhat.server.persistence.jpa.entity.Information;
import com.guesswhat.server.persistence.jpa.entity.Question;
import com.guesswhat.server.persistence.jpa.entity.QuestionIncubator;
import com.guesswhat.server.persistence.jpa.entity.Record;
import com.guesswhat.server.persistence.jpa.entity.User;

public class OfyService {
    static {
        factory().register(Image.class);
        factory().register(ImageHolder.class);
        factory().register(Information.class);
        factory().register(Question.class);
        factory().register(QuestionIncubator.class);
        factory().register(Record.class);
        factory().register(User.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}