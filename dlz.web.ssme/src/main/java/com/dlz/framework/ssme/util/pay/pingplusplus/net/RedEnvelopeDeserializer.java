package com.dlz.framework.ssme.util.pay.pingplusplus.net;

import java.lang.reflect.Type;

import com.dlz.framework.ssme.util.pay.pingplusplus.model.App;
import com.dlz.framework.ssme.util.pay.pingplusplus.model.ChargeRefundCollection;
import com.dlz.framework.ssme.util.pay.pingplusplus.model.RedEnvelope;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * Created by sunkai on 15/5/14.
 */
public class RedEnvelopeDeserializer implements JsonDeserializer<RedEnvelope> {
	void doNothing(){new java.util.ArrayList<>().forEach(a->{});}
    @Override
    public RedEnvelope deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject transFerJson = json.getAsJsonObject();
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).
                registerTypeAdapter(ChargeRefundCollection.class, new ChargeRefundCollectionDeserializer())
                .create();
        JsonElement appElement = transFerJson.get("app");
        RedEnvelope redEnvelope = gson.fromJson(json, RedEnvelope.class);

        if (null != appElement && appElement.isJsonObject()) {
            App app = gson.fromJson(appElement, App.class);
            redEnvelope.setApp(app);
        }
        return redEnvelope;
    }
}
