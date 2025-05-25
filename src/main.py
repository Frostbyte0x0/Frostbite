starting_options = ["entity", "item", "particle"]

FROSTBITE_LOCATION = "main/java/org/exodusstudio/frostbite/Frostbite.java"
RENDERER_REGISTRY_OFFSET = 4
LAYERS_LOCATION = "main/java/org/exodusstudio/frostbite/common/entity/client/layers/ModModelLayers.java"
LAYERS_OFFSET = 1
RENDERER_FOLDER_LOCATION = "main/java/org/exodusstudio/frostbite/common/entity/client/renderers"
MODEL_FOLDER_LOCATION = "main/java/org/exodusstudio/frostbite/common/entity/client/models"
ENTITY_FOLDER_LOCATION = "main/java/org/exodusstudio/frostbite/common/entity/custom"
ENTITY_REGISTRY_LOCATION = "main/java/org/exodusstudio/frostbite/common/registry/EntityRegistry.java"
ENTITY_REGISTRY_OFFSET = 3


def camel_to_snake(string: str):
    return "_".join([s.lower() for s in string.split("ABCDEFGHIJKLMNOPQRSTUVWXYZ")])

def snake_to_camel(string: str):
    return "".join([s.capitalize() for s in string.split("_")])


def select_from_menu(options: list[str]) -> int:
    for i in range(len(options)):
        print(str(i+1) + ": " + options[i].capitalize())

    out = int(input("> "))
    return out - 1


def load_template(file: str, camel_name: str):
    with open("templates/" + file + ".txt", "r") as f:
        template = f.read()

    snake_name = camel_to_snake(camel_name)

    template = (template.replace("^P", camel_name.capitalize())
                .replace("^S", snake_name)
                .replace("^U", snake_name.upper())
                .replace("^C", camel_name))

    return template


def write_to_file(path_to_file: str, text: str, line_offset: int, from_bottom: bool = True):
    with open(path_to_file, "r") as f:
        lines = f.readlines()

    index = len(lines) - line_offset if from_bottom else line_offset
    lines.insert(index, text)

    with open(path_to_file, "w") as f:
        f.writelines(lines)


def write_to_new_file(path: str, file_name: str, text: str):
    with open(path + "/" + file_name, "w") as f:
        f.write(text)


def start_entity(camel_name: str):
    # Model
    temp = load_template("entity_model", camel_name)
    write_to_new_file(MODEL_FOLDER_LOCATION, f"{camel_name.capitalize()}Model.java", temp)
    # Renderer
    temp = load_template("entity_renderer", camel_name)
    write_to_new_file(RENDERER_FOLDER_LOCATION, f"{camel_name.capitalize()}Renderer.java", temp)
    # Layers
    temp = load_template("entity_layer", camel_name)
    write_to_file(LAYERS_LOCATION, temp, LAYERS_OFFSET)
    # Actual entity class
    temp = load_template("entity_class", camel_name)
    write_to_new_file(ENTITY_FOLDER_LOCATION, f"{camel_name.capitalize()}.java", temp)
    # Register renderer
    temp = load_template("entity_renderer_registry", camel_name)
    write_to_file(FROSTBITE_LOCATION, temp, RENDERER_REGISTRY_OFFSET)
    # Register entity
    temp = load_template("entity_registry", camel_name)
    write_to_file(ENTITY_REGISTRY_LOCATION, temp, ENTITY_REGISTRY_OFFSET)


def start_item():
    # Item
    # - Item json
    # - Model json
    # - Custom ? custom class : just registry
    # - Register
    ...


def start_particle():
    ...


def start():
    name = input("Name (in camel case) > ")
    match select_from_menu(starting_options):
        case 0:
            start_entity(name)
        case 1:
            start_item()
        case 2:
            start_particle()


if __name__ == '__main__':
    start()
