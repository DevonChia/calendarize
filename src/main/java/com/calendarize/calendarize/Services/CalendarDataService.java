package com.calendarize.calendarize.Services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.calendarize.calendarize.Exceptions.CalendarIntegrationException;
import com.calendarize.calendarize.Repository.CalendarDataRepository;
import com.calendarize.calendarize.Util.EncryptionUtil;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;



@Service
public class CalendarDataService {
    private final CalendarDataRepository calendarDataRepository;

    public CalendarDataService(CalendarDataRepository calendarDataRepository) {
        this.calendarDataRepository = calendarDataRepository;
    }

    @Value("${google.calendar.api.client.encryption.secret}")
    private String secret;

    private Calendar initCalendarBuilder(String accessToken) {
        try {
            GoogleCredentials credentials = GoogleCredentials.create(
                new AccessToken(accessToken, null)
            );

            Calendar service = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
            )
            .setApplicationName("Calendarize")
            .build();

            return service;
        } catch (GeneralSecurityException | IOException e) {
            throw new CalendarIntegrationException("Failed to initialize Google Calendar configuration.", e);
        }
    }

    public List<Map<String, Object>> getGoogleCalendarData(String userId, String deviceId)
    {
        try {
            System.out.println("here inside get google data service");
            byte[] encryptedAccessToken = calendarDataRepository.getAccessToken("google", userId, deviceId);
            String accessToken = EncryptionUtil.decrypt(encryptedAccessToken, secret);
            System.out.println("got access token :: " + accessToken);

            Calendar service = initCalendarBuilder(accessToken);
            DateTime now = new DateTime(System.currentTimeMillis());

            Events events = service.events().list("primary") //TODO : set to primary for now
                // .setMaxResults(10)
                .setTimeMin(now)
                // .setTimeMax(now) //TODO : update setTimeMin and setTimeMax to dynamic render 
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

            List<Map<String, Object>> eventList = new ArrayList<>();
            for (Event event : events.getItems()) {
                Map<String, Object> data = new HashMap<>();

                DateTime start = event.getStart().getDateTime();
                String startDate;
                DateTime startTime = null;
                if (start != null) {
                    startDate = Instant.ofEpochMilli(start.getValue())
                                                    .atZone(ZoneId.of("Asia/Singapore"))
                                                    .format(DateTimeFormatter.ISO_LOCAL_DATE);
                } else {
                    startDate = event.getStart().getDate().toString();
                }

                DateTime end = event.getEnd().getDateTime();
                String endDate;
                DateTime endTime = null;
                if (end != null) {
                    endDate = Instant.ofEpochMilli(end.getValue())
                                                    .atZone(ZoneId.of("Asia/Singapore"))
                                                    .format(DateTimeFormatter.ISO_LOCAL_DATE);
                } else {
                    endDate = event.getEnd().getDate().toString();
                }

                data.put("summary", event.getSummary());
                data.put("startDate", startDate);
                data.put("startTime", startTime);
                data.put("endDate", endDate);
                data.put("endTime", endTime);

                eventList.add(data);
            }
            return eventList;
        } catch (IOException e) {
            throw new CalendarIntegrationException("Failed to initialize Google Calendar configuration.", e);
        }
    }
}
