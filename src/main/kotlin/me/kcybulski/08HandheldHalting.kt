package me.kcybulski

fun main() {
    val ops = lines("08HandheldHalting").map { parse(it) }

    val ctx = Context(program = ops)
    println(run(ctx))
}

fun run(ctx: Context): Int {
    val op = ctx.getInstruction()
    return when {
        op == null -> ctx.acc
        ctx.visited.contains(op) -> ctx.acc
        else -> run(op.next(ctx.copy(visited = ctx.visited + op)))
    }
}

private fun parse(line: String): Instruction {
    val split = line.split(" ")
    return when (split[0]) {
        "jmp" -> Jump(split[1].toInt())
        "acc" -> Acc(split[1].toInt())
        else -> Nothing(split[1].toInt())
    }
}

sealed class Instruction {
    abstract fun next(ctx: Context): Context
}

class Nothing(val arg: Int) : Instruction() {
    override fun next(ctx: Context) = ctx.copy(pointer = ctx.pointer + 1)
}

class Jump(val arg: Int) : Instruction() {
    override fun next(ctx: Context) = ctx.copy(pointer = ctx.pointer + arg)
}

class Acc(val arg: Int) : Instruction() {
    override fun next(ctx: Context) = ctx.copy(pointer = ctx.pointer + 1, acc = ctx.acc + arg)
}

class Arguments(val definitions: List<ArgumentDefinition>)

class ArgumentDefinition

data class Context(
    val acc: Int = 0,
    val pointer: Int = 0,
    private val program: List<Instruction>,
    val visited: List<Instruction> = emptyList(),
) {

    val size = program.size
    fun getInstruction() = program.getOrNull(pointer)

}
