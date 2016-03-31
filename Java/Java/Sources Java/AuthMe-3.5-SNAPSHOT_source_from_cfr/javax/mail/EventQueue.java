/*
 * Decompiled with CFR 0_110.
 */
package javax.mail;

import java.util.Vector;
import javax.mail.event.MailEvent;

class EventQueue
implements Runnable {
    private QueueElement head = null;
    private QueueElement tail = null;
    private Thread qThread;

    public EventQueue() {
        this.qThread = new Thread((Runnable)this, "JavaMail-EventQueue");
        this.qThread.setDaemon(true);
        this.qThread.start();
    }

    public synchronized void enqueue(MailEvent event, Vector vector) {
        QueueElement newElt = new QueueElement(event, vector);
        if (this.head == null) {
            this.head = newElt;
            this.tail = newElt;
        } else {
            newElt.next = this.head;
            this.head.prev = newElt;
            this.head = newElt;
        }
        this.notifyAll();
    }

    private synchronized QueueElement dequeue() throws InterruptedException {
        while (this.tail == null) {
            this.wait();
        }
        QueueElement elt = this.tail;
        this.tail = elt.prev;
        if (this.tail == null) {
            this.head = null;
        } else {
            this.tail.next = null;
        }
        elt.next = null;
        elt.prev = null;
        return elt;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void run() {
        try {
            do {
                QueueElement qe = this.dequeue();
                MailEvent e = qe.event;
                Vector v = qe.vector;
                for (int i = 0; i < v.size(); ++i) {
                    try {
                        e.dispatch(v.elementAt(i));
                        continue;
                    }
                    catch (Throwable t) {
                        if (t instanceof InterruptedException) return;
                    }
                }
                qe = null;
                e = null;
                v = null;
            } while (true);
        }
        catch (InterruptedException e) {
            // empty catch block
        }
    }

    void stop() {
        if (this.qThread != null) {
            this.qThread.interrupt();
            this.qThread = null;
        }
    }

    static class QueueElement {
        QueueElement next = null;
        QueueElement prev = null;
        MailEvent event = null;
        Vector vector = null;

        QueueElement(MailEvent event, Vector vector) {
            this.event = event;
            this.vector = vector;
        }
    }

}

