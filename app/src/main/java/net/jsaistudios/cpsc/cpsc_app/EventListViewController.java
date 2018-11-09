package net.jsaistudios.cpsc.cpsc_app;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.jsaistudios.cpsc.cpsc_app.EventsPage.EventsFunctions;
import net.jsaistudios.cpsc.cpsc_app.EventsPage.EventsObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class EventListViewController extends ListViewController {
    RequestQueue queue;
    String fbPageKey = "EAAfROW4QQZCABAG7pP3oonYJTTJEqJ17jXRUh0I7yIvxIsZAHhfkZACChZCowTS3m33K9LtFJNcEV7gVlVJ4EenBcj1fWJqxXeyNQbZBz5R883qqANaLHZBVORztaA88f9elYwATNwEPPkYbP4bkdhHB5qG5vOB9sU9g9i89jWmhRUs02MctS4HZArFn9NG1C1TXCCCtKaxDAZDZD";
    public EventListViewController(PageSpecificFunctions funcs, Context c, ListViewModel lm) {
        listViewModel = lm;

        pageSpecificFunctions = funcs;
        lm.setPageSpecificFunctions(pageSpecificFunctions);
        context = c;
        queue = Volley.newRequestQueue(context);
        listViewModel.setCreationObserver(new Observer() {
            @Override
            public void update() {
                listViewModel.setRecyclerAdapter(pageSpecificFunctions.getRecyclerAdapter(context, listViewModel));
            }
        });
        getEventsList();
    }

    private void getEventsList() {
        EventsObject object = new EventsObject();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("events");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<RecyclerModel> responseList = new ArrayList<>();
                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    RecyclerModel pm = new RecyclerModel();
                    EventsObject object = new EventsObject();
                    object.setName(snap.child("name").getValue(String.class));
                    object.setInfo(snap.child("info").getValue(String.class));
                    object.setDate(EventsFunctions.fbDateToNormal(snap.child("date").getValue(String.class)));
                    if(snap.hasChild("fbId")) {
                        object.setId(snap.child("fbId").getValue(String.class));
                    } else {
                        object.setId("CPSCCustom");
                    }
                    object.setPlace(snap.child("place").getValue(String.class));
                    pm.setItemObject(object);
                    responseList.add(pm);
                }
                listViewModel.getModelView().getDataModelList().clear();
                listViewModel.getModelView().getDataModelList().addAll(responseList);
                Comparator stringDateComparator = new Comparator<RecyclerModel>() {
                    @Override
                    public int compare(RecyclerModel recyclerModel, RecyclerModel t1) {
                        return ((EventsObject)t1.getItemObject()).getDate().compareTo(((EventsObject)recyclerModel.getItemObject()).getDate());
                    }
                };
                Collections.sort(listViewModel.getModelView().getDataModelList(), stringDateComparator);
                parseJSONEventList(input);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
    private void parseJSONEventList(String response) {
        try {
            JSONObject events = new JSONObject(response);
            final JSONArray eList = events.getJSONArray("data");
            List<RecyclerModel> responseList = new ArrayList<>();
            label1:
            for (int i=0; i<eList.length(); i++) {
                RecyclerModel pm = new RecyclerModel();
                EventsObject object = new EventsObject();
                final JSONObject ev = eList.getJSONObject(i);
                for(RecyclerModel rm: listViewModel.getModelView().getDataModelList()) {
                    if(ev.getString("id").equals(((EventsObject)rm.getItemObject()).getId())) {
                        continue label1;
                    }
                }
                object.setName(ev.getString("name"));
                object.setInfo(ev.getString("description"));
                object.setDate(EventsFunctions.fbDateToNormal(ev.getString("start_time")));
                object.setId(ev.getString("id"));
                object.setPlace(ev.getJSONObject("place").getString("name").replace("T", " ").replace("-0700",""));
                pm.setItemObject(object);
                responseList.add(pm);
            }
            addNewFbEvent(eList);
            listViewModel.getModelView().getDataModelList().addAll(responseList);
            Comparator stringDateComparator = new Comparator<RecyclerModel>() {
                @Override
                public int compare(RecyclerModel recyclerModel, RecyclerModel t1) {
                    return ((EventsObject)t1.getItemObject()).getDate().compareTo(((EventsObject)recyclerModel.getItemObject()).getDate());
                }
            };
            Collections.sort(listViewModel.getModelView().getDataModelList(), stringDateComparator);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addNewFbEvent(final JSONArray eList) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("events");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int i=0; i<eList.length(); i++) {
                    try {
                        JSONObject ev = eList.getJSONObject(i);
                            DatabaseReference newEv = myRef.child(ev.getString("id"));
                            newEv.child("name").setValue(ev.getString("name"));
                            newEv.child("info").setValue(ev.getString("description"));
                            newEv.child("fbId").setValue(ev.getString("id"));
                            newEv.child("date").setValue(ev.getString("start_time"));
                            newEv.child("place").setValue(ev.getJSONObject("place").getString("name").replace("T", " ").replace("-0700", ""));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
    private void getFacebookList() {
        String url ="https://graph.facebook.com/v3.1/382506115285435/events";
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        parseJSONEventList(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());
                        getDatabaseList();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + fbPageKey);
                return params;
            }
        };
        queue.add(getRequest);
    }

    public Date makeDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return new Date(0);
        }
    }
    String input = "{\"data\":[{\"description\":\"What is Big Trip all about you may be asking? \uD83C\uDFBFONLY THE BEST WEEK OF YOUR ENTIRE LIFE \\r\\n\\r\\nCPSC, along with CPPRR, will be sending it hard into the only hole everyone will want to explore. JACKSON HOLE! ☃️\\r\\n\\r\\nWhat does this trip include? (Besides endless fun and beer bongs) \\r\\nTransportation \\r\\n4/5 days of lift tickets \\r\\n5 nights of lodging \\r\\nExxxclusive entry into nightly events \\r\\n\\r\\nFor more information check out the PowerPoint on this page or hit up any of your board members \uD83D\uDE0F\\r\\n\\r\\nSign up today to reserve your spot !! http://bit.ly/BTJackson2018 \\r\\nWe can’t wait to see you on the slopes! ⛷\",\"end_time\":\"2018-12-23T00:00:00-06:00\",\"name\":\"Big Trip 2018 Jackson Hole\",\"place\":{\"name\":\"Jackson Hole Mountain Resort\",\"location\":{\"city\":\"Teton Village\",\"country\":\"United States\",\"latitude\":43.58759501949,\"longitude\":-110.82811571045,\"state\":\"WY\",\"zip\":\"83025\"},\"id\":\"24718308041\"},\"start_time\":\"2018-12-15T09:00:00-06:00\",\"id\":\"470336703456928\"},{\"description\":\"Super excited to announce that we will be playing at The Mark next Wednesday! Come see us play from 10-1:30!\\r\\n\\r\\nFollow I2I:\\r\\n-- Soundcloud: https://soundcloud.com/i2imusic\\r\\n-- Instagram: https://www.instagram.com/i2i_music\\r\\n-- Facebook: https://www.facebook.com/i2ibeats/\\r\\n-- Twitter: https://twitter.com/DJ_I2I\",\"end_time\":\"2018-10-18T03:30:00-05:00\",\"name\":\"I2I at The Mark\",\"place\":{\"name\":\"The Mark\",\"location\":{\"city\":\"San Luis Obispo\",\"country\":\"United States\",\"latitude\":35.27835,\"longitude\":-120.66472,\"state\":\"CA\",\"street\":\"673 Higuera St\",\"zip\":\"93401\"},\"id\":\"176757623037383\"},\"start_time\":\"2018-10-18T00:00:00-05:00\",\"id\":\"2170353269952483\"},{\"description\":\"Come be a beach bum with us at Pirates! Reconnect with your fellow ski clubbers after a long summer of sending. All are welcome, we encourage you to bring your friends who havent joined ski club yet!!!\\r\\n\\r\\nYou will easily spot us by our DJ set up, plethora of liquids to drink, cpsc EZ up, and flag soaring high in the sky.\\r\\n\\r\\nBring a towel I guess. Maybe some sun screen if you're about that life. Clothing is always optional ;) \\r\\n\\r\\nWe will be offering rides to Pirates so tell all your wowies they have nothing to worry about! We will load up cars at the Slack parking lot starting at 12:40. The last car will leave at 1:15. If you don’t get your toosh in a car, you gotta find you’re own way to the party. \\r\\n\\r\\nHit up a board member if you are looking into a ride so we make sure we have enough cars. AKA if you’re a big timer pls drive yourself. We wanna make sure the wowies have priority cuz we love the WOWIESSSS \uD83D\uDC9B\",\"end_time\":\"2018-09-19T19:00:00-05:00\",\"name\":\"Ski Club WOW: Beach Day\",\"place\":{\"name\":\"Pirate's Cove\",\"location\":{\"city\":\"San Luis Obispo\",\"country\":\"United States\",\"latitude\":35.175873308033,\"longitude\":-120.71929201768,\"state\":\"CA\",\"street\":\"Cave Landing Rd\",\"zip\":\"93424\"},\"id\":\"218236892022913\"},\"start_time\":\"2018-09-19T15:00:00-05:00\",\"id\":\"324159081493132\"},{\"description\":\"Come eat pizza, drink beer, and socialize! For the 21+ this will be the meet up location before heading out to the bars. There will still be plenty of us hanging out at Woodstock's if bar crawls aren't your thing. \\r\\nMore the merrier, so bring your friends and support one of our local sponsors!\",\"end_time\":\"2018-09-19T00:00:00-05:00\",\"name\":\"Ski Club WOW: Pizza & Bar Crawl\",\"place\":{\"name\":\"Woodstock's Pizza SLO\",\"location\":{\"city\":\"San Luis Obispo\",\"country\":\"United States\",\"latitude\":35.2813461,\"longitude\":-120.6607242,\"state\":\"CA\",\"street\":\"1000 Higuera St\",\"zip\":\"93401\"},\"id\":\"57334040358\"},\"start_time\":\"2018-09-18T21:00:00-05:00\",\"id\":\"2166130600125682\"},{\"description\":\"The only orientation you need is straight into SKI CLUB! \\r\\nJoin Ski Club on a crazy scavenger hunt filled with raunchy riddles, goofy games, and sexy schenanigans! Starting conveniently close to campus, you will earn points by completing tasks and earning enough will get you access to the after party of the century!\\r\\nYou can begin the scavenger hunt any time from 5-7:30\\r\\nHit up one of your sexy board members for the starting address and we will guide you onto your next location from there!\",\"end_time\":\"2018-09-18T01:00:00-05:00\",\"name\":\"Ski Club WOW: Scavenger Hunt\",\"place\":{\"name\":\"contact a board member\"},\"start_time\":\"2018-09-17T19:00:00-05:00\",\"id\":\"316146579148644\"},{\"description\":\"It's time to die up while sippin down some cold ones! We will provide all the tie dying materials and you can either bring your own clothing to tie dye or we will have some extra white T-shirts that you can purchase for $5! We will have beer die games going on while we tie dye and its going to be ridiculously fun so don't miss it! We hope to see all the senders there and bring a wowie!\",\"end_time\":\"2018-09-16T19:00:00-05:00\",\"name\":\"Ski Club WOW: Tie-\\\"Die\\\" Party\",\"place\":{\"name\":\"TBD\",\"location\":{\"city\":\"Los Angeles\",\"country\":\"United States\",\"latitude\":33.9697897,\"longitude\":-118.2468148,\"state\":\"CA\",\"zip\":\"90001\"},\"id\":\"1375698299200106\"},\"start_time\":\"2018-09-16T16:00:00-05:00\",\"id\":\"266915760609736\"},{\"description\":\"Ski club loves wowiesssss!! Everyone is welcome to come, especially wowies ;) Come drink our booze and meet new people cuz thats what were all about! Contact a board member for the address and we will make sure to give you a weekend you'll remember from today until next WOW\",\"end_time\":\"2018-09-16T02:00:00-05:00\",\"name\":\"Ski Club WOW: 4 the WOWies\",\"place\":{\"name\":\"TBD\"},\"start_time\":\"2018-09-15T23:00:00-05:00\",\"id\":\"264875170816589\"},{\"description\":\"Its going to be a beautiful day to soak up the sun and drink down some nice cold brews. \\r\\nCome join us and meet tons of new friends while we grill up delicious hot dogzzz and burgers! \\r\\n\\r\\nWe will have our membership booth here so if you still need to sign up for a membership you can today :)\\r\\n\\r\\nIf you are interested in hosting this awesome event we are willing to compensate with liquid currency, love, and/or a pineapple\",\"end_time\":\"2018-09-14T22:00:00-05:00\",\"name\":\"Ski Club WOW: BBQ n' Brews\",\"place\":{\"name\":\"contact a board member\"},\"start_time\":\"2018-09-14T18:00:00-05:00\",\"id\":\"1062100113952714\"},{\"description\":\"The time has come once again to JOIN SKI CLUB!!! Come on down to sign up for the year, put down your big trip deposit, and meet/hang with your awesome board members!\\r\\n\\r\\nWe will be kicking off this years farmer's booth on September 20th at 6pm. We have a booth at Farmer's every week of fall quarter, so make sure to come say hi! Our booth is located in front of Kruzeberg Coffee!\\r\\n\\r\\nMembership for the year is $40. This includes a sick membership shirt, access to all of our social events, sweet discounts for all over town, and the experience of a lifetime!!! \\r\\n\\r\\nFall quarter we host weekend events, beach days, and bar crawls.\\r\\nWinter quarter we head to the mountains every other weekend! We shred in Tahoe, Kirkwood, Mammoth, Oregon, and Utah.\\r\\n\\r\\nRemember, you have to rejoin every year, so get your cute booty down to farmer's! ;)\",\"end_time\":\"2018-09-13T23:00:00-05:00\",\"name\":\"Ski Club WOW: Farmer's Market\",\"place\":{\"name\":\"Downtown SLO Farmers' Market\",\"location\":{\"city\":\"San Luis Obispo\",\"country\":\"United States\",\"latitude\":35.280659865588,\"longitude\":-120.66155433655,\"state\":\"CA\",\"zip\":\"93401\"},\"id\":\"44091802612\"},\"start_time\":\"2018-09-13T20:00:00-05:00\",\"id\":\"517877188673742\"},{\"description\":\"Let the WOWies know who we are by riding around town reppin SKI CLUB!! \\r\\n\\r\\nLet us write (in paint-safe, washable, non-toxic paint) on your car! This is an awesome way to show your support and recruit more senders to join us!\\r\\n\\r\\nAs an incentive to let us use your shiny car as a canvas, you can pick up some sick SWAG. We will have shirts, stickers, and more special goodies!\",\"end_time\":\"2018-09-13T18:00:00-05:00\",\"name\":\"Ski Club WOW: Car Painting\",\"place\":{\"name\":\"Lizzies House\"},\"start_time\":\"2018-09-13T15:00:00-05:00\",\"id\":\"1983614401929854\"},{\"description\":\"Funk With Us, Central Pacific Ski Club, & EOS Lounge Present \\r\\n\\r\\nSNBRN\\r\\n\\r\\nEarly Bird tickets starting at $10!\\r\\n\\r\\nTickets: https://snbrnslo.nightout.com/\\r\\n\\r\\n18+ to party, 21+ to drink | Please bring a VALID I.D\\r\\n\\r\\nDoors at 9pm\",\"end_time\":\"2018-05-24T03:30:00-05:00\",\"name\":\"Funk With Us Presents: SNBRN at The Graduate 5.23.18\",\"place\":{\"name\":\"The Graduate\",\"location\":{\"city\":\"San Luis Obispo\",\"country\":\"United States\",\"latitude\":35.2536697285,\"longitude\":-120.63894663446,\"state\":\"CA\",\"street\":\"990 Industrial Way\",\"zip\":\"93401\"},\"id\":\"37499337937\"},\"start_time\":\"2018-05-23T23:00:00-05:00\",\"id\":\"2136930039925326\"},{\"description\":\"Do you wanna build a snowman? \\r\\nWell TOO BAD!\\r\\nbut you can cum get bippity boppity boozey with us on our Dirty Disney Cruise instead ;)\\r\\nBe our guest bright and early on May 11 at Lake Don Pedro and get ready to see a whole new world.\\r\\n\\r\\nAt $150.69, the price of admission includes the bear necessities:\\r\\n- Wild weekend of shenanigans \\r\\n- Sweet tank\\r\\n- Exclusive CPSC hat\\r\\n- Enough food to satisfy 101 dalmatians\\r\\n- All the booze you could wish upon a star for\\r\\n\\r\\nSIGN UPS ARE LIVE!!\\r\\nhttp://cpsconline.com/houseboating-2018\",\"end_time\":\"2018-05-13T22:00:00-05:00\",\"name\":\"CPSC Houseboating 2018: Dirty Disney Cruise\",\"place\":{\"name\":\"Lake Don Pedro\",\"location\":{\"city\":\"La Grange\",\"country\":\"United States\",\"latitude\":37.66124,\"longitude\":-120.32112,\"state\":\"CA\",\"street\":\"2411 Hildago\",\"zip\":\"95329\"},\"id\":\"677199279002770\"},\"start_time\":\"2018-05-11T10:00:00-05:00\",\"id\":\"134014664117782\"},{\"description\":\"Calling all ravers and party people for the biggest event of the year, SNOCHELLA!! Get ready to get wild! CPSC is taking partying to a new level with this one, you won't want to miss it! \\r\\n\\r\\nTickets are $5 for ski club members and $10 for non-members. A pregame and buses to and from the show will be available for another $5 for ski club members and $10 for non-members. Venmo @cpsc69 for tickets and buses. Contact a board member with any questions you may have!\\r\\n\\r\\nHeadliner:\\r\\n\\r\\nBRØHG \uD83C\uDF34\\r\\n----------------------------\\r\\nFB - https://www.facebook.com/brohgofficial/\\r\\nIG - https://www.instagram.com/brohgofficial/\\r\\nSC - https://soundcloud.com/brohgofficial\\r\\n\\r\\nWith support from:\\r\\nAlt9\\r\\ndelta T\\r\\nMazza\",\"end_time\":\"2018-04-26T03:00:00-05:00\",\"name\":\"Snochella\",\"place\":{\"name\":\"The Graduate\",\"location\":{\"city\":\"San Luis Obispo\",\"country\":\"United States\",\"latitude\":35.2536697285,\"longitude\":-120.63894663446,\"state\":\"CA\",\"street\":\"990 Industrial Way\",\"zip\":\"93401\"},\"id\":\"37499337937\"},\"start_time\":\"2018-04-25T22:00:00-05:00\",\"id\":\"357713951391738\"},{\"description\":\"Funk With Us, EOS Lounge, The Graduate, & Central Pacific Ski Club come together to deliver another party to the central coast. Sevenn will be gracing us with their booty shaking bass lines. Supported by our friends at This Bangs April 4th is sure to be a party you won't want to miss. We can't wait for you all to #FunkWithUs again!\\r\\n\\r\\n18+ to dance & 21+ to stumble / PLEASE BRING VALID I.D\\r\\n\\r\\nSevenn:\\r\\nSoundcloud: https://soundcloud.com/whoareseven\\r\\nIG: https://www.instagram.com/sevenn/\\r\\nFacebook: https://www.facebook.com/sevennofficial/\",\"end_time\":\"2018-04-05T04:00:00-05:00\",\"name\":\"Funk With Us Presents: Sevenn at SLO GRAD 4.4.18\",\"place\":{\"name\":\"The Graduate\",\"location\":{\"city\":\"San Luis Obispo\",\"country\":\"United States\",\"latitude\":35.2536697285,\"longitude\":-120.63894663446,\"state\":\"CA\",\"street\":\"990 Industrial Way\",\"zip\":\"93401\"},\"id\":\"37499337937\"},\"start_time\":\"2018-04-04T23:00:00-05:00\",\"id\":\"226541251237354\"},{\"description\":\"WHADDUP PARTY PEOPLE?!?? Are you ready to get wet and wild this with thousands of other college students? We sure are! And to make it even better...the drinking age is 18!!!\\r\\n\\r\\nThis spring break, March 26-30, Central Pacific Ski Club has teamed up with SWAT Events and Rockstar to bring you the biggest baddest party west of the Mississippi! You do not have to be a ski club member to send it with us this time, everyone is welcome :)\\r\\n\\r\\nAll-inclusive packages start at just $349! There are additional options to drive yourself, provide own lodging, etc.\\r\\n\\r\\n$100 DEPOSIT IS DUE on THURSDAY FEBRUARY 15th to save your spot so you can be with our squad!! :) \\r\\n\\r\\nIncluded in your package:\\r\\n-4 Nights Beachfront Hotel @ Rosarito Beach Hotel\\r\\n-Round-Trip Shuttles from San Diego to Mexico \\r\\n-Day Events & Parties \\r\\n-Nightly Concerts with headliners like  Louis the Child, Ekali,     Denzel Curry, & Sevenn\\r\\n-Free All-You-Can-Eat Buffet Every Day & Night\\r\\n-Top-Shelf Open-Bar Parties \\r\\n-Free cover to All SWAT Events\\r\\n-Exclusive Drink Specials \\r\\n-Welcome Party Upon Arrival\\r\\n-Spring Break Olympics\\r\\n-24-Hour American First Aid & Security\\r\\n-24-Hour American SWAT Staff\\r\\n\\r\\nYou MUST have a valid passport to enter into Mexico and to return to the United States. \\r\\n\\r\\nSign up with Ski Club:\\r\\n<http://bit.ly/cpscspringbreak>\",\"end_time\":\"2018-03-30T18:20:00-05:00\",\"name\":\"CPSC x SWAT Spring Break Rosarito!\",\"place\":{\"name\":\"Rosarito Beach Baja\",\"location\":{\"city\":\"Rosarito\",\"country\":\"Mexico\",\"latitude\":32.339248792076,\"longitude\":-117.05729127428,\"street\":\"907 Benito Juarez Blvd.\",\"zip\":\"22710\"},\"id\":\"357694499884\"},\"start_time\":\"2018-03-26T13:00:00-05:00\",\"id\":\"1018902688250556\"},{\"description\":\"Funk With Us, EOS Lounge, & Central Pacific Ski Club are bringing ANGELZ to The Graduate for a night of G-House vibes!\\r\\nBe sure to grab your tickets soon and always remember to #FunkWithUs\\r\\n\\r\\nEarly bird tickets starting at $5!!!\\r\\nInfo: funkwithus.nightout.com\\r\\n\\r\\n18+ to party, 21+ to drink | Please bring a VALID I.D\",\"end_time\":\"2018-03-08T04:00:00-06:00\",\"name\":\"Funk With Us Presents: ANGELZ\",\"place\":{\"name\":\"The Graduate\",\"location\":{\"city\":\"San Luis Obispo\",\"country\":\"United States\",\"latitude\":35.2536697285,\"longitude\":-120.63894663446,\"state\":\"CA\",\"street\":\"990 Industrial Way\",\"zip\":\"93401\"},\"id\":\"37499337937\"},\"start_time\":\"2018-03-07T23:00:00-06:00\",\"id\":\"1591341397640267\"},{\"description\":\"What up senderoonis! Cum with us on the last small trip of winter quarter!! We will be shredding hard the weekend of March 1st. \\r\\n\\r\\nThis legendary trip is $140.69 and includes a 3-night stay in our mountain home,  all meals, and beverages.\\r\\n\\r\\nSign-ups will go LIVE on TUESDAY FEBRUARY 20th at 9pm on cpsconline.com. This will be our smallest trip of the season so don't miss this opportunity to send it with the elite crew!\\r\\n\\r\\nCarpool meeting will be the Tuesday before we leave, where you can get some sweet lift ticket deals!\",\"end_time\":\"2018-03-04T13:00:00-06:00\",\"name\":\"CPSC Small Trip Mammoth 2018\",\"place\":{\"name\":\"Mammoth Ski Resort\",\"location\":{\"city\":\"Mammoth Lakes\",\"country\":\"United States\",\"latitude\":37.76812325,\"longitude\":-119.09074203333,\"state\":\"CA\",\"zip\":\"93529\"},\"id\":\"157202407658931\"},\"start_time\":\"2018-03-01T18:20:00-06:00\",\"id\":\"636318656492453\"},{\"description\":\"We are heading to the land of mormons, sister wives, and most importantly POWDER! We have reserved the biggest house known to man (7500 sq ft) which means there is plenty of room for activities and senders. Sign up for the trip of a lifetime on THIS MONDAY JANUARY 29th at 9pm at cpsconline.com - have your fingers ready because the spots will fill up in approximately 4.20 seconds!\\r\\n\\r\\nWe will have a meeting to organize car pools and leave either on Thursday February 15 or Friday February 16 and return on Monday February 19 (Presidents Day which means no school!). The trip costs $140 which includes lodging for 3 nights, food, beverages, and access to discounted lift tickets. Slopes just a short drive away include Alta, Snowbird, Brighton, or Park City.\",\"end_time\":\"2018-02-19T17:20:00-06:00\",\"name\":\"CPSC Small Trip Utah 2018!\",\"place\":{\"name\":\"Snowbird\",\"location\":{\"city\":\"Snowbird Lodge\",\"country\":\"United States\",\"latitude\":40.581040217024,\"longitude\":-111.65667035911,\"state\":\"UT\",\"street\":\"9385 S Snowbird Center Dr\",\"zip\":\"84092-9000\"},\"id\":\"93382780324\"},\"start_time\":\"2018-02-15T17:20:00-06:00\",\"id\":\"2011084535773935\"},{\"description\":\"Funk With Us, Central Pacific Ski Club, & EOS Lounge Present \\r\\nDirtybird Valenties Day\\r\\n\\r\\nw/ Ardalan\\r\\nJonah Vii\\r\\nBix King Kaufman\\r\\nDJ YKD B2B House Arrest\\r\\nLove Sponge\\r\\n\\r\\nEarly Bird tickets starting at $10!\\r\\n\\r\\nTickets: https://nightout.com/events/dirtybirdvday/tickets\\r\\n\\r\\n18+ to party, 21+ to drink | Please bring a VALID I.D\\r\\n\\r\\nDoors at 8pm\",\"end_time\":\"2018-02-15T03:30:00-06:00\",\"name\":\"Funk With Us Presents Ardalan at SLO Grad 2.14.17\",\"place\":{\"name\":\"The Graduate\",\"location\":{\"city\":\"San Luis Obispo\",\"country\":\"United States\",\"latitude\":35.2536697285,\"longitude\":-120.63894663446,\"state\":\"CA\",\"street\":\"990 Industrial Way\",\"zip\":\"93401\"},\"id\":\"37499337937\"},\"start_time\":\"2018-02-14T22:00:00-06:00\",\"id\":\"485007725227814\"},{\"description\":\"Do you often find yourself frolicking in the snow? Are you a champion beer bonger? Do you find joy in throwing the biggest baddest parties on the central coast? You should consider running for Central Pacific Ski Club Executive Board for the 2018-2019 school year!\\r\\n\\r\\nThe ridiculous ritual of our new board selection is about to begin!  Come find out all that you need to know at our info meeting on Tuesday at 8pm in Baker. Make sure to review the position descriptions to get an idea of what you want to run for! Choose your top 3 ;) \\r\\n\\r\\nWhat's board like?\\r\\n-Make 10 of the closest friends you never knew you wanted\\r\\n-Throw bangers every weekend of fall quarter\\r\\n-Enjoy ski trips for free every other weekend of winter quarter\\r\\n-Facilitate the legendary houseboating scandal of 2018\\r\\n-this list could literally go on infinitely...cum to the meeting ;) \\r\\n\\r\\nWhat you need to apply:\\r\\n-positive attitude and love for all things!\\r\\n-availability from March 2018-2019 (you must be in SLO)\\r\\n-availability over spring break to attend Fam Trip!\\r\\n\\r\\nTIMELINE OF SUCCESS\\r\\nFeb 13th: info meeting, pick up application  \\r\\nFeb 28th: turn in application & resume, schedule interview time\\r\\nMarch 5-9th: interviews\\r\\nMarch 10th: ERECTIONS!!!\\r\\n\\r\\nSee you on Tuesday!\",\"end_time\":\"2018-02-13T23:00:00-06:00\",\"name\":\"CPSC Executive Board Info Meeting\",\"place\":{\"name\":\"Warren J. Baker Center for Science and Mathematics\",\"location\":{\"city\":\"San Luis Obispo\",\"country\":\"United States\",\"latitude\":35.301301229783,\"located_in\":\"24743274366\",\"longitude\":-120.66053907571,\"state\":\"CA\",\"zip\":\"93407\"},\"id\":\"145831848948234\"},\"start_time\":\"2018-02-13T22:00:00-06:00\",\"id\":\"175631223211410\"},{\"description\":\"Funk With Us, Central Pacific Ski Club, & EOS Lounge are excited to announce that Dr. Fresch will be bringing his \\\"Gangsta, Gangsta\\\" sound to The Graduate!\\r\\n\\r\\nEarly Bird tickets starting at $10!\\r\\n\\r\\nTickets: https://nightout.com/events/dr-fresch-at-slo-graduate-18-11718/tickets\\r\\n\\r\\n18+ to party, 21+ to drink | Please bring a VALID I.D\\r\\n\\r\\n9pm doors open\\r\\n\\r\\n9pm-10pm: Alt9\\r\\n10pm-11pm: Cpt. Fabulous\\r\\n11pm-12am: DJ YKD B2B House Arrest\\r\\n12am-1:30am (close): Dr. Fresch\\r\\n\\r\\nLighting & Sound provided by Solstice Productions (DJ Solstice)\",\"end_time\":\"2018-01-18T03:45:00-06:00\",\"name\":\"Dr. Fresch at The Graduate SLO 1/17/18\",\"place\":{\"name\":\"The Graduate\",\"location\":{\"city\":\"San Luis Obispo\",\"country\":\"United States\",\"latitude\":35.2536697285,\"longitude\":-120.63894663446,\"state\":\"CA\",\"street\":\"990 Industrial Way\",\"zip\":\"93401\"},\"id\":\"37499337937\"},\"start_time\":\"2018-01-17T23:00:00-06:00\",\"id\":\"1136482046454135\"},{\"description\":\"The mountains are calling!! We swapped Utah and Oregon this year because it’s DUMPING in Bachelor right now. Missing Big Trip?? Sign up on Monday for a snowy getaway to look forward to!! Send it to Oregon with us for the FIRST SMALL TRIP of the season!!\\r\\n\\r\\nLast year this trip filled up in a WHOPPING 6.9 sexonds, so be ready to hit “FULL SEND” on MONDAY DECEMBER 4th at 9pm!!!\\r\\n\\r\\nFor just $140, you can enjoy our luxurious mountain lodge for the long weekend \uD83C\uDFBF \\r\\n\\r\\nPrice includes:\\r\\n-4 night stay in our sweet house\\r\\n-breakfast, lunch, & dinner every day \\r\\n-liquids to keep ya going \\r\\n-themed parties every night \\r\\n\\r\\nWe carpool to all of our small trips to reduce our carbon footprint \uD83D\uDC63 ride meeting will be hosted the Tuesday before the trip. \\r\\n\\r\\nWe will offer discounted lift tickets closer to the date.\",\"end_time\":\"2018-01-15T06:20:00-06:00\",\"name\":\"CPSC SMALL TRIP OREGON!\",\"place\":{\"name\":\"Mt Bachelor\",\"location\":{\"city\":\"Bend\",\"country\":\"United States\",\"latitude\":44.002751600404,\"longitude\":-121.67830668619,\"state\":\"OR\",\"street\":\"13000 SW Century Dr\",\"zip\":\"97702\"},\"id\":\"130989650816\"},\"start_time\":\"2018-01-11T18:20:00-06:00\",\"id\":\"1980548928899544\"},{\"description\":\"You are minutes away from registering for the best week of your college career…the 2017-2018 Ski & Snowboard Trip!  Join us and your closest friends in the mountains for an unforgettable week of skiing, partying, and mountain activities!!\\r\\n\\r\\nEnjoy 5 nights in luxurious mountainside lodging surrounded by some of the best beginner and expert terrain in North America. Spend your days on the slopes or with one of the many non-skier activities. Rock out during the evenings at the après ski events and parties only ski club can create! Get ready for the best week of your entire life!\\r\\n\\r\\nWe'll be headed there with CAL POLY PAMONA and OL'MISS UNIVERSITY!\\r\\n\\r\\nWITH A SPECIAL MUSICAL GUEST TBD!!\\r\\n\\r\\nSee you on the slopes!\",\"end_time\":\"2017-12-16T22:00:00-06:00\",\"name\":\"CPSC Big Trip 2017: Steamboat, Colorado\",\"place\":{\"name\":\"Steamboat Resort\",\"location\":{\"city\":\"Steamboat Springs\",\"country\":\"United States\",\"latitude\":40.457543684226,\"longitude\":-106.8035973288,\"state\":\"CO\",\"street\":\"2305 Mount Werner Cir\",\"zip\":\"80487\"},\"id\":\"92646793962\"},\"start_time\":\"2017-12-09T19:00:00-06:00\",\"event_times\":[{\"id\":\"120228565357515\",\"start_time\":\"2017-12-16T19:00:00-06:00\",\"end_time\":\"2017-12-16T22:00:00-06:00\"},{\"id\":\"120228568690848\",\"start_time\":\"2017-12-15T19:00:00-06:00\",\"end_time\":\"2017-12-15T22:00:00-06:00\"},{\"id\":\"120228562024182\",\"start_time\":\"2017-12-14T19:00:00-06:00\",\"end_time\":\"2017-12-14T22:00:00-06:00\"},{\"id\":\"120228558690849\",\"start_time\":\"2017-12-13T19:00:00-06:00\",\"end_time\":\"2017-12-13T22:00:00-06:00\"},{\"id\":\"120228552024183\",\"start_time\":\"2017-12-12T19:00:00-06:00\",\"end_time\":\"2017-12-12T22:00:00-06:00\"},{\"id\":\"120228545357517\",\"start_time\":\"2017-12-11T19:00:00-06:00\",\"end_time\":\"2017-12-11T22:00:00-06:00\"},{\"id\":\"120228555357516\",\"start_time\":\"2017-12-10T19:00:00-06:00\",\"end_time\":\"2017-12-10T22:00:00-06:00\"},{\"id\":\"120228548690850\",\"start_time\":\"2017-12-09T19:00:00-06:00\",\"end_time\":\"2017-12-09T22:00:00-06:00\"}],\"id\":\"120228542024184\"},{\"description\":\"Start your Thanksgiving Break off with a BANGGG!  Come pre-party with CPSC this Friday! Hit up a board member around 3 PM on Friday for the address!\\r\\n\\r\\nWe will be sending it in style to Avila Beach, first PARTYYYY bus leaves at 5 PM, location TBD.\\r\\n\\r\\nEveryone going on the bus is welcome to the pregame :)\\r\\n\\r\\nTo secure your spot on the party bus venmo @kelsglas\\r\\nCPSC members: $12\\r\\nNon-members: $16\",\"end_time\":\"2017-11-17T21:00:00-06:00\",\"name\":\"CPSC Presents Kid Cudi PreParty and Party Buses!\",\"place\":{\"name\":\"Sext a board member for the address on Friday at 3pm!\"},\"start_time\":\"2017-11-17T18:00:00-06:00\",\"id\":\"143189396406779\"},{\"description\":\"CPSC is going to be taking Avila Beach by STORM! Come party with us and take our buses to safely arrive to and from the venue!\\r\\nBuses Prices:\\r\\n\\r\\n$12 for members\\r\\n$16 for non members\\r\\n\\r\\nSign up below!\",\"end_time\":\"2017-11-11T22:00:00-06:00\",\"name\":\"CPSC Presents the Horizon Tour PreParty\",\"place\":{\"name\":\"TBD\"},\"start_time\":\"2017-11-11T18:00:00-06:00\",\"id\":\"1738414889523160\"}],\"paging\":{\"cursors\":{\"before\":\"QVFIUm9SakZAtMXYzdm05ZAFZAoTnhJdzZAJRnV4bEZATdkxKYTMta2dzVTNGY3JDb2pDWmxGUU5tVGpmU0tYNmoxb2FJckRfMWY3T25jOGh3ZAmZADUEJWUXhrQXRn\",\"after\":\"QVFIUjhackdaeDBnT0FIdFMyY2RZAdTUxenRfNno1ZA1RQY09IdFNqLWRaNVNXYjVXYWx4YXhhSHB3c0lybGFzMmJITEhla1A2TFo5SWFEZATVIUFVUYWY1djBR\"},\"next\":\"https://graph.facebook.com/v3.1/382506115285435/events?access_token=EAAfROW4QQZCABAAMH6NQNJq3vfCcCYRj2MkJ5sZAfNEnizJiDLJ6LQhbJjcDaolwgcYXpxWRtf2VuEm1bTlW6tESl8isaNHgOXvQ1dTtY4JZAUKxGV4PvevUmyjXKyf8zs1QxDikuUsLR2NNaJwZBx0iil58W6WnT2RacZAC28u1pFH1GmOjGU5VP7x0wTM0ZD&pretty=0&limit=25&after=QVFIUjhackdaeDBnT0FIdFMyY2RZAdTUxenRfNno1ZA1RQY09IdFNqLWRaNVNXYjVXYWx4YXhhSHB3c0lybGFzMmJITEhla1A2TFo5SWFEZATVIUFVUYWY1djBR\"}}";
}
