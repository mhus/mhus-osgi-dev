/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.osgi.dev.jpa.impl;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.lib.core.MCast;
import de.mhus.osgi.api.karaf.AbstractCmd;

@Command(scope = "mhus", name = "dev-jpa", description = "JPA tests")
@Service
public class CmdDevJpa extends AbstractCmd {

    protected static String DS_NAME = "h2";

    @Argument(
            index = 0,
            name = "cmd",
            required = true,
            description = "Command to execute",
            multiValued = false)
    String cmd;

    @Argument(
            index = 1,
            name = "paramteters",
            required = false,
            description = "Parameters",
            multiValued = true)
    String[] parameters;

    @Option(name = "-p", description = "Provider Match")
    String provMatch;

    @Override
    public Object execute2() throws Exception {

        if (cmd.equals("ds")) {
            DS_NAME = parameters[0];
            if (parameters.length > 1) TestPersistenceUnitInfo.HIBERNATE_DIALECT = parameters[1];
            System.out.println("DataSource: " + DS_NAME);
            System.out.println("Dialect: " + TestPersistenceUnitInfo.HIBERNATE_DIALECT);
            return null;
        }
        if (cmd.equals("autocommit")) {
            TestPersistenceUnitInfo.AUTOCOMMIT = MCast.toboolean(parameters[0], false);
            System.out.println("Autocommit: " + TestPersistenceUnitInfo.AUTOCOMMIT);
        }

        if (cmd.equals("benchmark")) {
            Benchmark.benchmark(provMatch, parameters);
        }

        if (cmd.equals("adb-test01")) {
            AdbTest01.test();
        }

        if (cmd.equals("test01")) {
            Test01.test(provMatch, parameters);
        }
        return null;
    }
}
