package com.ycyw.support.infrastructure.adapter.directory.conversation;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ycyw.support.domain.model.entity.conversation.ConversationMessage;
import com.ycyw.support.domain.model.valueobject.conversation.MessageSender;
import com.ycyw.support.domain.port.directory.MessageDirectory;
import com.ycyw.support.infrastructure.adapter.service.messages.MessageService;

import org.eclipse.jdt.annotation.Nullable;

@Component
public class MessageQueryDirectory implements MessageDirectory {
  private final MessageService messageRepository;

  public MessageQueryDirectory(MessageService messageRepository) {
    this.messageRepository = messageRepository;
  }

  @Override
  public @Nullable ConversationMessage find(UUID id) {
    final MessageService.@Nullable Message message = messageRepository.find(id);
    if (message == null) {
      return null;
    }
    return toDomain(message);
  }

  @Override
  public List<ConversationMessage> findAll(UUID conversationId) {
    return messageRepository.findAll(conversationId).stream().map(this::toDomain).toList();
  }

  private ConversationMessage toDomain(MessageService.Message m) {
    return ConversationMessage.hydrate(
        m.id(), m.conversation(), m.message(), new MessageSender(m.senderType(), m.sender()));
  }

  //   La solution à ce dilemme n'est pas de changer votre schéma d'agrégat (qui est bon pour
  // l'écriture), mais d'adopter une approche où le modèle pour lire les données est différent du
  // modèle pour les écrire. C'est le principe du CQRS (Command Query Responsibility Segregation).

  //     Le Modèle d'Écriture (Write Model) : C'est ce que vous avez déjà conçu ! Votre agrégat
  // Conversation est parfait pour gérer les commandes comme AddMessageCommand,
  // ChangeConversationSubjectCommand, etc. Il garantit la cohérence des données.

  //     Le Modèle de Lecture (Read Model) : Pour les besoins d'affichage et de recherche, vous
  // allez créer des "projections" de données optimisées. Ce sont des modèles simples, souvent
  // dénormalisés, qui ne servent qu'à être lus.

  //         Vous pourriez avoir une table SQL message_summary avec juste les colonnes nécessaires :
  // message_id, content_preview, sender_name, sent_at, conversation_id.

  //         Vous créeriez alors un service de lecture, qu'on n'appelle pas Repository pour éviter
  // la confusion, mais plutôt MessageFinder ou MessageQueryService.

  //         Ce service interrogerait directement votre modèle de lecture et retournerait des DTOs
  // (Data Transfer Objects) simples, pas des objets de domaine complexes.

  // Comment ça fonctionne en pratique ?

  // Le flux typique est basé sur les événements :

  //     Une commande AddMessage est exécutée via votre agrégat Conversation.

  //     Le ConversationRepository sauvegarde l'état de la conversation.

  //     L'agrégat publie un événement de domaine, par exemple MessageWasAddedToConversation.

  //     Un "gestionnaire d'événement" (event handler) écoute cet événement.

  //     Ce gestionnaire met à jour les modèles de lecture optimisés (par exemple, il insère une
  // nouvelle ligne dans la table message_summary).

}
