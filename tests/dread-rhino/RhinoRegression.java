import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.ContextFactory;
import dev.latvian.mods.rhino.Scriptable;
import java.util.List;

/** Scripting-engine regression test, not a live Minecraft server test. */
public final class RhinoRegression {
    public static void main(String[] args) {
        Context cx = new ContextFactory().enter();
        Scriptable oldScope = cx.initStandardObjects();
        cx.addToScope(oldScope, "javaTypes", List.of("minecraft:zombie", "minecraft:skeleton", "minecraft:cow"));
        try {
            cx.evaluateString(oldScope,
                "function oldRegistryPass(){ for (const entityType of javaTypes) { const id = String(entityType); } } for(var run=0;run<1000;run++) oldRegistryPass();",
                "old-java-registry-loop.js", 1, null);
            System.out.println("NOTE: simplified old loop did not reproduce the error outside KubeJS; live server log remains the failure evidence.");
        } catch (Exception error) {
            System.out.println("Old Java-backed loop result: " + error.getMessage());
        }
        Scriptable fixedScope = cx.initStandardObjects();
        cx.addToScope(fixedScope, "javaTypes", List.of("minecraft:zombie", "minecraft:skeleton", "minecraft:cow"));
        Object result = cx.evaluateString(fixedScope, """
            var mockRegistry = {iterator:function(){return javaTypes.iterator();},getKey:function(t){return t;}};
            function fixedRegistryPass(){
                var candidates=[];
                var typeIterator=mockRegistry.iterator();
                while(typeIterator.hasNext()){
                    var entityType=typeIterator.next();
                    var registryId=String(mockRegistry.getKey(entityType));
                    var undead=registryId!=='minecraft:cow';
                    if(undead)candidates.push(registryId);
                }
                return candidates;
            }
            function fixedPlayerPass(players){
                var stages=[];
                for(var playerIndex=0;playerIndex<players.size();playerIndex++){
                    var player=players.get(playerIndex);
                    var kills=player.kills;
                    var stage=kills>=50?2:kills>=10?1:0;
                    stages.push(stage);
                }
                return stages;
            }
            var mockPlayers={size:function(){return 4;},get:function(i){return [{kills:9},{kills:10},{kills:49},{kills:50}][i];}};
            function fixedKillPass(types){var sum=0;for(var typeIndex=0;typeIndex<types.length;typeIndex++){var entityType=types[typeIndex];sum+=entityType==='minecraft:zombie'?7:3;}return sum;}
            for(var repeat=0;repeat<1000;repeat++){
                var types=fixedRegistryPass();
                if(types.join(',')!=='minecraft:zombie,minecraft:skeleton')throw Error('Registry failure');
                if(fixedPlayerPass(mockPlayers).join(',')!=='0,1,1,2')throw Error('Player iteration failure');
                if(fixedKillPass(types)!==10)throw Error('Kill count failure');
            }
            'PASS: 1000 repeated Java-backed registry, player, and kill-count loop passes on Rhino 2101.2.8-build.91';
            """, "fixed-scheduler-loops.js", 1, null);
        System.out.println(result);
    }
}
