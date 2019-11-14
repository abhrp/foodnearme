package com.abhrp.foodnearme.domain.executor

import io.reactivex.Scheduler

/**
 * PostExecutionThread - Interface for adding execution thread for RxJava
 *
 * @see Scheduler
 */
interface PostExecutionThread {

    /**
     * Return the Scheduler needed for RxJava execution
     *
     * @return Scheduler for execution
     */
    val scheduler: Scheduler?
}