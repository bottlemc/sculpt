package com.github.glassmc.sculpt.v1_8_9.test;

import com.github.glassmc.loader.loader.ITransformer;
import com.github.glassmc.loader.util.Identifier;
import net.minecraft.client.MinecraftClient;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class SculptTransformer implements ITransformer {

    private final String MINECRAFT_CLIENT = Identifier.parse("net/minecraft/client/MinecraftClient").getClassName();
    private final String GAME_RENDERER = Identifier.parse("net/minecraft/client/render/GameRenderer").getClassName();
    private final String SCREEN = Identifier.parse("net/minecraft/client/gui/screen/Screen").getClassName();

    @Override
    public byte[] transform(String name, byte[] data) {
        if(name.equals(MINECRAFT_CLIENT)) {
            ClassNode classNode = this.getClassNode(data);
            this.transformMinecraftClient(classNode);
            return this.getData(classNode);
        }
        if(name.equals(GAME_RENDERER)) {
            ClassNode classNode = this.getClassNode(data);
            this.transformGameRenderer(classNode);
            return this.getData(classNode);
        }
        if(name.equals(SCREEN)) {
            ClassNode classNode = this.getClassNode(data);
            this.transformScreen(classNode);
            return this.getData(classNode);
        }
        return data;
    }

    private void transformMinecraftClient(ClassNode classNode) {
        Identifier tick = Identifier.parse("net/minecraft/client/MinecraftClient#tick()V");
        String tickName = tick.getMethodName();
        String tickDescription = tick.getMethodDesc();

        for(MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(tickName) && methodNode.desc.equals(tickDescription)) {
                for(AbstractInsnNode node : methodNode.instructions.toArray()) {
                    if(node instanceof MethodInsnNode && ((MethodInsnNode) node).name.equals("getEventButton")) {
                        MethodInsnNode insert = new MethodInsnNode(Opcodes.INVOKESTATIC, Hook.class.getName().replace(".", "/"), "onAction", "()V");
                        methodNode.instructions.insertBefore(node, insert);
                    }
                }
            }
        }

        Identifier handleKeyInput = Identifier.parse("net/minecraft/client/MinecraftClient#handleKeyInput()V");
        String handleKeyInputName = handleKeyInput.getMethodName();
        String handleKeyInputDescription = handleKeyInput.getMethodDesc();

        for(MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(handleKeyInputName) && methodNode.desc.equals(handleKeyInputDescription)) {
                methodNode.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, Hook.class.getName().replace(".", "/"), "onKey", "()V"));
            }
        }
    }

    private void transformGameRenderer(ClassNode classNode) {
        Identifier render = Identifier.parse("net/minecraft/client/render/GameRenderer#render(FJ)V");
        String renderName = render.getMethodName();
        String renderDescription = render.getMethodDesc();

        for(MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(renderName) && methodNode.desc.equals(renderDescription)) {
                for(AbstractInsnNode node : methodNode.instructions.toArray()) {
                    if(node.getOpcode() == Opcodes.RETURN) {
                        MethodInsnNode insert = new MethodInsnNode(Opcodes.INVOKESTATIC, Hook.class.getName().replace(".", "/"), "onRender", "()V");
                        methodNode.instructions.insertBefore(node, insert);
                    }
                }
            }
        }
    }

    private void transformScreen(ClassNode classNode) {
        Identifier render = Identifier.parse("net/minecraft/client/gui/screen/Screen#handleMouse()V");
        String renderName = render.getMethodName();
        String renderDescription = render.getMethodDesc();

        for(MethodNode methodNode : classNode.methods) {
            if(methodNode.name.equals(renderName) && methodNode.desc.equals(renderDescription)) {
                MethodInsnNode insert = new MethodInsnNode(Opcodes.INVOKESTATIC, Hook.class.getName().replace(".", "/"), "onAction", "()V");
                methodNode.instructions.insert(insert);
            }
        }
    }

    private ClassNode getClassNode(byte[] data) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(data);
        classReader.accept(classNode, 0);
        return classNode;
    }

    private byte[] getData(ClassNode classNode) {
        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

}