### 1导入依赖jboss-marshalling

```java
		<dependency>
            <groupId>org.jboss.marshalling</groupId>
            <artifactId>jboss-marshalling</artifactId>
            <version>1.3.0.CR9</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.jboss.marshalling/jboss-marshalling-serial -->
        <dependency>
            <groupId>org.jboss.marshalling</groupId>
            <artifactId>jboss-marshalling-serial</artifactId>
            <version>1.3.0.CR9</version>
        </dependency>
```



### 2创建MarshallingDecoder和MarshallingEncoder

```java
 public static MarshallingEncoder buildMarshallingEncoder() {
        //1 创建 MarshallingEncoder
        //2 创建 MarshallerProvider
        //3 创建 MarshallerFactory  MarshallingConfiguration
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, configuration);
        MarshallingEncoder encoder = new MarshallingEncoder(provider);
        return encoder;
    }

    public static MarshallingDecoder buildMarshallingDecoder() {
        //1 创建 MarshallingDecoder
        //2 创建 MarshallerProvider
        //3 创建 MarshallerFactory  MarshallingConfiguration
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);
        MarshallingDecoder decoder = new MarshallingDecoder(provider);
        return decoder;
    }

```



### 3添加解码器

```java
ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingDecoder())
                                    		  	.addLast(MarshallingCodeFactory.buildMarshallingEncoder())
             .addLast(new MarshallerHandler07());
```

