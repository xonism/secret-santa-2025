package xonism.secretrandomizer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xonism.secretrandomizer.model.Email;
import xonism.secretrandomizer.model.SecretSantaRequest;
import xonism.secretrandomizer.model.SecretSantaRequest.Participant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecretSantaService {

    private static final String EMAIL_SUBJECT = "🎁 Mission Briefing: Operation Secret Santa";
    private static final String EMAIL_MESSAGE_TEMPLATE = """
            Agent %s,<br><br>
            
            Your covert assignment has been authorized. Intel confirms that <b>you have been selected for a highly classified holiday mission</b>.<br><br>
            
            Your target: <b>%s</b><br>
            Your objective: <b>Procure a Steam gift that sparks joy, confusion, or mild emotional chaos (dealer’s choice).</b><br><br>
            
            Remember:<br>
            – Maintain secrecy.<br>
            – Avoid suspicious jingling.<br>
            – Deny everything if confronted by carolers.<br><br>
            
            Good luck, Agent.<br>
            The holidays are counting on you.""";

    private final Random random = new Random();

    private final EmailService emailService;

    public void randomize(SecretSantaRequest request) {
        Map<Participant, Participant> gifterToGiftee = getGifterToGiftee(request);
        gifterToGiftee.forEach((gifter, giftee) -> {
            Email email = getEmail(gifter, giftee);
            emailService.send(email);
        });
    }

    private Map<Participant, Participant> getGifterToGiftee(SecretSantaRequest request) {
        List<Participant> gifters = new ArrayList<>(request.participants());
        List<Participant> giftees = new ArrayList<>(request.participants());

        do {
            Collections.shuffle(giftees, random);
        } while (!isValidAssignment(gifters, giftees));

        Map<Participant, Participant> gifterToGiftee = new HashMap<>();
        for (int i = 0; i < gifters.size(); i++) {
            gifterToGiftee.put(gifters.get(i), giftees.get(i));
        }
        return gifterToGiftee;
    }

    private boolean isValidAssignment(List<Participant> gifters, List<Participant> giftees) {
        int numberOfParticipants = gifters.size();

        for (int i = 0; i < numberOfParticipants; i++) {
            Participant gifter = gifters.get(i);
            Participant giftee = giftees.get(i);

            if (gifter.equals(giftee)) {
                return false;
            }

            // check to avoid pairs where a -> b and b -> a
            int index = gifters.indexOf(giftee);
            if (index >= 0 && giftees.get(index).equals(gifter)) {
                return false;
            }
        }

        return true;
    }

    private Email getEmail(Participant gifter, Participant giftee) {
        String message = EMAIL_MESSAGE_TEMPLATE.formatted(gifter.name(), giftee.name());
        return new Email(gifter.emailAddress(), EMAIL_SUBJECT, message);
    }
}
