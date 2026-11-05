import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.ContextFactory;
import dev.latvian.mods.rhino.Scriptable;

/** Executes the failing and replacement loop patterns in the installed Rhino release.
 * This is a scripting-engine regression test, not a live Minecraft server test. */
public final class RhinoRegression {
    public static void main(String[] args) {
        Context cx = new ContextFactory().enter();
        Scriptable brokenScope = cx.initStandardObjects();
        boolean reproduced = false;
        try {
            cx.evaluateString(brokenScope,
                "function badRegistryPass(){ for (const entityType of [1,2,3]) { const id = String(entityType); } } badRegistryPass();",
                "old-registry-loop.js", 1, null);
        } catch (Exception error) {
            if (!String.valueOf(error.getMessage()).contains("redeclaration")) throw error;
            reproduced = true;
            System.out.println("REPRODUCED original loop: " + error.getMessage());
        }
        if (!reproduced) throw new AssertionError("Expected the original loop redeclaration error");
        Scriptable fixedScope = cx.initStandardObjects();
        Object result = cx.evaluateString(fixedScope, """
            var mockTypes = ['minecraft:zombie','minecraft:skeleton','minecraft:cow'];
            var mockRegistry = {iterator:function(){var n=0;return {hasNext:function(){return n<mockTypes.length;},next:function(){return mockTypes[n++];}};},getKey:function(t){return t;}};
            function fixedRegistryPass(){
                var candidates=[];
                var typeIterator=mockRegistry.iterator();
                while(typeIterator.hasNext()){
                    var entityType=typeIterator.next();
                    var registryId=String(mockRegistry.getKey(entityType));
                    var undead=registryId!=='minecraft:cow';
                    if(undead)candidates.push(entityType);
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
            'PASS: 1000 repeated registry, player, and kill-count loop passes on Rhino 2101.2.8-build.91';
            """, "fixed-scheduler-loops.js", 1, null);
        System.out.println(result);
    }
}
