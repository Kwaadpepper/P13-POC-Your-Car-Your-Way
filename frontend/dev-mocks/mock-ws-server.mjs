import { randomUUID } from "crypto";
import { createServer } from "http";
import { WebSocketServer } from "ws";


const server = createServer();
const wss = new WebSocketServer({ server });

/** Mémoire */
const rooms = new Map(); // conversationId -> Set<WebSocket>
const messagesByRoom = new Map(); // conversationId -> ChatMessage[]

function broadcast(conversationId, msg, excludeWs) {
  const clients = rooms.get(conversationId);
  if (!clients) return;
  for (const ws of clients) {
    if (ws.readyState === ws.OPEN && ws !== excludeWs) {
      ws.send(JSON.stringify(msg));
    }
  }
}

function ensureRoom(id) {
  if (!rooms.has(id)) rooms.set(id, new Set());
  if (!messagesByRoom.has(id)) messagesByRoom.set(id, []);
}

wss.on("connection", (ws, req) => {
  const url = new URL(req.url || "", "http://localhost");
  const token = url.searchParams.get("token") || "";
  // Déduit le rôle via le token (ex: "client-123", "operator-42")
  let role = token.startsWith("operator") ? "operator" : "client";
  let userId = token || `user-${Math.floor(Math.random() * 1000)}`;

  ws.userId = userId;
  ws.role = role;
  ws.joined = new Set(); // conversations

  ws.on("message", (raw) => {
    let msg;
    try {
      msg = JSON.parse(raw.toString());
    } catch {
      return;
    }
    const { type, payload } = msg || {};

    if (type === "join") {
      const { conversationId } = payload;
      ensureRoom(conversationId);
      rooms.get(conversationId).add(ws);
      ws.joined.add(conversationId);

      // Envoie au nouveau venu la liste des participants
      const participants = [...rooms.get(conversationId)].map((s) => ({
        userId: s.userId,
        role: s.role,
        status: "online",
      }));
      ws.send(JSON.stringify({ type: "joined", payload: { conversationId, participants } }));

      // Signale présence aux autres
      const presence = { type: "presence", payload: { userId, role, status: "online", conversationId } };
      broadcast(conversationId, presence, ws);

    } else if (type === "leave") {
      const { conversationId } = payload;
      rooms.get(conversationId)?.delete(ws);
      ws.joined.delete(conversationId);
      broadcast(conversationId, { type: "presence", payload: { userId, role, status: "offline", conversationId } });

    } else if (type === "message") {
      const { conversationId, text } = payload;
      ensureRoom(conversationId);
      const chatMsg = {
        id: randomUUID(),
        conversationId,
        from: userId,
        role,
        text,
        sentAt: new Date().toISOString(),
      };
      messagesByRoom.get(conversationId).push(chatMsg);
      // Fanout à tous (y compris l'expéditeur pour avoir l'horodatage/uniformité)
      broadcast(conversationId, { type: "message", payload: chatMsg });
      if (ws.readyState === ws.OPEN) ws.send(JSON.stringify({ type: "message", payload: chatMsg }));

    } else if (type === "typing") {
      const { conversationId, isTyping } = payload;
      const evt = { type: "typing", payload: { userId, role, conversationId, isTyping } };
      broadcast(conversationId, evt, ws);

    } else if (type === "getHistory") {
      const { conversationId, limit = 50 } = payload;
      const all = messagesByRoom.get(conversationId) || [];
      const slice = all.slice(-limit);
      ws.send(JSON.stringify({ type: "history", payload: { conversationId, messages: slice } }));
    }
  });

  ws.on("close", () => {
    for (const conversationId of ws.joined) {
      rooms.get(conversationId)?.delete(ws);
      broadcast(conversationId, { type: "presence", payload: { userId, role, status: "offline", conversationId } });
    }
  });
});

const PORT = process.env.PORT || 7071;
server.listen(PORT, () => console.log(`WS mock server on ws://localhost:${PORT}`));
