```
     y
  /     \
 m       d
  \     /
   i - i
```

# mydii

***MYDII*** (***M***YDII ***Y***outh ***D***ependency ***I***njection ***I***mplementation)
is Dependency Injection implementation.

## Flow

```
DependencyFactory (Constructor)
           \
            v
  DependencySetter (Setter)
             \
              v
    DependencyInitializer (PostConstruct)
               \
                v
      DependencyProxyFactory (Proxy)
```
