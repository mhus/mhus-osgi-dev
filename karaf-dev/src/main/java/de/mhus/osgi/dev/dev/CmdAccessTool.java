/**
 * Copyright (C) 2018 Mike Hummel (mh@mhus.de)
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
package de.mhus.osgi.dev.dev;

import java.util.ArrayList;
import java.util.List;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.LifecycleUtils;

import de.mhus.lib.core.M;
import de.mhus.lib.core.aaa.Aaa;
import de.mhus.lib.core.aaa.AccessApi;
import de.mhus.lib.core.aaa.SubjectEnvironment;
import de.mhus.osgi.api.MOsgi;
import de.mhus.osgi.api.aaa.RealmServiceProvider;
import de.mhus.osgi.api.karaf.AbstractCmd;

@Command(
        scope = "mhus",
        name = "access-tool",
        description = "Access Control - tool")
@Service
public class CmdAccessTool extends AbstractCmd {

    @Argument(
            index = 0,
            name = "cmd",
            required = true,
            description =
                    "Command to execute\n",
            multiValued = false)
    String cmd;

    @Argument(
            index = 1,
            name = "paramteters",
            required = false,
            description = "Parameters",
            multiValued = true)
    String[] parameters;

    @Override
    public Object execute2() throws Exception {

        if (cmd.equals("info")) {
            AccessApi api = M.l(AccessApi.class);
            System.out.println("API: " + api.getClass().getCanonicalName());
            SecurityManager manager = api.getSecurityManager();
            System.out.println("Manager: " + manager.getClass().getCanonicalName());
            if (manager instanceof DefaultSecurityManager) {
                DefaultSecurityManager def = (DefaultSecurityManager)manager;
                System.out.println("Realms:");
                def.getRealms().forEach(r -> System.out.println("  " + r.getName() + " " + r.getClass().getCanonicalName()));
                //TODO more ...
            }
        } else
        if (cmd.equals("resetrealms")) {
            List<Realm> realms = new ArrayList<>();
            for (RealmServiceProvider service : MOsgi.getServices(RealmServiceProvider.class, null)) {
                System.out.println("Add Realm " + service.getService().getName() + " " + service.getService().getClass().getCanonicalName());
                realms.add(service.getService());
            }
            LifecycleUtils.init(realms);
            DefaultSecurityManager manager = new DefaultSecurityManager(realms);
            SecurityUtils.setSecurityManager(manager);
        } else
        if (cmd.equals("reloadrealms")) {
            List<Realm> realms = new ArrayList<>();
            for (RealmServiceProvider service : MOsgi.getServices(RealmServiceProvider.class, null)) {
                System.out.println("Add Realm " + service.getService().getName() + " " + service.getService().getClass().getCanonicalName());
                realms.add(service.getService());
            }
            LifecycleUtils.init(realms);
            ((DefaultSecurityManager)SecurityUtils.getSecurityManager()).setRealms(realms);
        } else
        if (cmd.equals("login")) {
            System.out.println(Aaa.getPrincipal());
            String ticket = Aaa.createAccountTicket(parameters[0], parameters[1]);
            Subject subject = Aaa.login(ticket);
            try (SubjectEnvironment access = Aaa.asSubject(subject)) {
                System.out.println(Aaa.getPrincipal());
                System.out.println(Aaa.getPrincipalData());
            }
            System.out.println(Aaa.getPrincipal());
        }
        return null;
    }
}
