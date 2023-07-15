/**
 * Contains default behavior for commenting on a contact.
 * <p>
 * Comments about the person are made according to certain topics.<br>
 * Each commentator determines itself the topics to use to comment.<br>
 * This directory contains default commentators for commenting on a person.<br>
 * We can also call the topics that the default commentators determined for commenting on a person as the default topics.<br>
 * That's the most important point, topics.<br>
 * What are the default topics?
 *
 * <p>
 * <p>
 * <h3> - The number of call logs</h3>
 * <p>
 * > The number of call records of all contacts in the contacts is calculated,
 * and the people with the highest number of records are evaluated.<br>
 * This topic can be divided into subtopics,
 * for example, the most incoming calls and the most outgoing calls are valuable subtopics.<br>
 * Also, the most missed calls and the most rejected calls are valuable subtopics too.<br>
 * <p>
 * <p>
 * <h3> - The duration of call history</h3>
 * <p>
 * > The duration of call history of all contacts is calculated,
 * and the people with the highest duration of call history are evaluated.<br>
 * Duration of call history is not the taken duration between the end of the call and the start of the call.<br>
 * Duration of call history is the taken duration between the first call and the last call made
 * in the call logs, which belongs to the contacts.<br>
 * ------------------------------------------------------------------------------------------------------------<br>
 * <p>
 * <p>
 * <p>
 * According to topics, commenting on a contact needs to all contacts and all call logs.<br>
 * <p>
 */
package com.tr.hsyn.telefonrehberi.main.contact.comment.defaults;
