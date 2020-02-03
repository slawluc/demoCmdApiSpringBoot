package com.example.demo.framework.command.blocking

import com.example.demo.framework.command.CommandRequest
import com.example.demo.framework.common.Outcome

interface Command<C: CommandRequest, R>: Function1<C, Outcome<R>>