package com.revup.notification_service;

import com.revup.notification_service.dto.NotificationMessage;
import com.revup.notification_service.model.Notification;
import com.revup.notification_service.repository.NotificationRepository;
import com.revup.notification_service.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = {
	"spring.rabbitmq.host=localhost",
	"spring.datasource.url=jdbc:h2:mem:testdb",
	"spring.datasource.driver-class-name=org.h2.Driver",
	"spring.jpa.hibernate.ddl-auto=create-drop"
})
class NotificationServiceApplicationTests {

	@MockBean
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private NotificationService notificationService;

	@BeforeEach
	void setUp() {
		notificationRepository.deleteAll();
	}

	@Test
	void contextLoads() {
		assertNotNull(notificationService);
		assertNotNull(notificationRepository);
	}

	@Test
	void testSaveNotification() {
		Notification notification = new Notification();
		notification.setUserId(1L);
		notification.setType("APPOINTMENT_CONFIRMED");
		notification.setMessage("Your appointment has been confirmed");
		notification.setRead(false);

		Notification saved = notificationService.save(notification);

		assertNotNull(saved.getId());
		assertEquals(1L, saved.getUserId());
		assertEquals("APPOINTMENT_CONFIRMED", saved.getType());
		assertFalse(saved.isRead());
	}

	@Test
	void testFindNotificationsByUserId() {
		Notification notif1 = new Notification();
		notif1.setUserId(1L);
		notif1.setType("TYPE_A");
		notif1.setMessage("Message A");
		notificationRepository.save(notif1);

		Notification notif2 = new Notification();
		notif2.setUserId(1L);
		notif2.setType("TYPE_B");
		notif2.setMessage("Message B");
		notificationRepository.save(notif2);

		Notification notif3 = new Notification();
		notif3.setUserId(2L);
		notif3.setType("TYPE_C");
		notif3.setMessage("Message C");
		notificationRepository.save(notif3);

		List<Notification> user1Notifications = notificationService.getForUser(1L);
		assertEquals(2, user1Notifications.size());
	}

	@Test
	void testSendToAppointment() {
		NotificationMessage message = new NotificationMessage(
			"APPOINTMENT_UPDATED",
			123L,
			1L,
			"Appointment Update",
			"Your appointment has been updated"
		);

		notificationService.sendToAppointment(123L, message);

		verify(messagingTemplate, times(1))
			.convertAndSend(eq("/topic/appointments.123"), eq(message));
	}

	@Test
	void testSendToUser() {
		NotificationMessage message = new NotificationMessage(
			"PROGRESS_UPDATE",
			null,
			1L,
			"Progress Update",
			"Your service is 50% complete"
		);

		notificationService.sendToUser(1L, message);

		verify(messagingTemplate, times(1))
			.convertAndSendToUser(eq("1"), eq("/queue/notifications"), eq(message));
	}

	@Test
	void testSendToAppointmentWithNullId() {
		NotificationMessage message = new NotificationMessage();
		notificationService.sendToAppointment(null, message);

		verify(messagingTemplate, never()).convertAndSend(anyString(), (Object) any());
	}

	@Test
	void testSendToUserWithNullId() {
		NotificationMessage message = new NotificationMessage();
		notificationService.sendToUser(null, message);

		verify(messagingTemplate, never()).convertAndSendToUser(anyString(), anyString(), (Object) any());
	}
}
