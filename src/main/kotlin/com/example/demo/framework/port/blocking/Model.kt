package com.example.demo.framework.port.blocking

import com.example.demo.framework.common.Outcome
import com.example.demo.framework.port.PortRequest

interface Port<C: PortRequest, R>: Function1<C, Outcome<R>>