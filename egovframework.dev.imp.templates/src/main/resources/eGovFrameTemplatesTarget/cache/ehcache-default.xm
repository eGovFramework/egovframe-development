<ehcache>
    <diskStore path="user.dir/second"/>
    <defaultCache maxElementsInMemory="10000" 
                  eternal="false" 
                  timeToIdleSeconds="120" 
                  timeToLiveSeconds="120" 
                  overflowToDisk="true" 
                  diskPersistent="true"  
                  diskExpiryThreadIntervalSeconds="120"/>
 
    <cache name="cache"
           maxElementsInMemory="100"
           eternal="false"
           timeToIdleSeconds="360"
           timeToLiveSeconds="1000"
           overflowToDisk="false"
           diskPersistent="false"  
           memoryStoreEvictionPolicy="LRU">
    </cache>    
</ehcache>
